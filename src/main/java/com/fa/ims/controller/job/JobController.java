package com.fa.ims.controller.job;

import com.fa.ims.constant.CommonConstants;
import com.fa.ims.controller.BaseController;
import com.fa.ims.dto.*;
import com.fa.ims.entity.*;
import com.fa.ims.enums.InterviewScheduleStatus;
import com.fa.ims.enums.JobLevel;
import com.fa.ims.enums.JobStatus;
import com.fa.ims.service.*;
import com.fa.ims.util.EnumUtils;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/jobs")

public class JobController extends BaseController {

    private final SkillService skillService;
    private final BenefitService benefitService;
    private final JobService jobService;

    private final JobSkillService jobSkillService;

    private final JobBenefitService jobBenefitService;

    @GetMapping("/create")
    public String showCreateJob(Model model) {
        JobParamDto jobParamDto = new JobParamDto();
        model.addAttribute("jobParamDto", jobParamDto);

        initSkillList(model);
        initBenefitList(model);

        return "job/create-job";
    }

    @PostMapping("/create")
    public String createJob(@ModelAttribute("jobParamDto") @Valid JobParamDto jobParamDto,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            initSkillList(model);
            initBenefitList(model);
            return "job/create-job";
        }
        boolean isValid = false;
        if (jobParamDto.getSalaryFrom() == null){
            jobParamDto.setSalaryFrom(BigDecimal.valueOf(0));
        }
        if (jobParamDto.getSalaryFrom().compareTo(jobParamDto.getSalaryTo()) > 0){
            bindingResult.rejectValue("salaryTo", "Message.097");
            isValid = true;
        } else if (!jobParamDto.getStartDate().isBefore(jobParamDto.getEndDate())) {
            bindingResult.rejectValue("endDate", "Message.096");
            isValid = true;
        }
        if (isValid) {
            initSkillList(model);
            initBenefitList(model);
            return "job/create-job";
        }
        Job job = new Job();
        BeanUtils.copyProperties(jobParamDto, job);

        job.setJobLevel(Arrays.stream(jobParamDto.getJobLevel())
                .map(JobLevel::getDisplayValue)
                .collect(Collectors.joining(", ")));

        if (jobParamDto.getSkillIdList() != null) {
            Set<JobSkill> jobSkillSet = Arrays.stream(jobParamDto.getSkillIdList())
                    .map(skillId -> new JobSkill(Skill.builder().id(skillId).build(), job))
                    .collect(Collectors.toSet());
            job.setRequiredSkillSet(jobSkillSet);
        }
        if (jobParamDto.getBenefitIdList() != null) {
            Set<JobBenefit> jobBenefitSet = Arrays.stream(jobParamDto.getBenefitIdList())
                    .map(benefitId -> new JobBenefit(Benefit.builder().id(benefitId).build(), job))
                    .collect(Collectors.toSet());
            job.setJobBenefitSet(jobBenefitSet);
        }
        try{
            jobService.createNew(job);
            redirectAttributes.addFlashAttribute("messageSuccess","Message.089");
            return "redirect:/jobs";
        } catch (Exception e){
            initSkillList(model);
            initBenefitList(model);
            model.addAttribute("messageFail","Message.088");
            return "job/create-job";
        }
    }

    private void initSkillList(Model model) { //Get all skill form DB
        List<Skill> skillList = skillService.findAll();
        List<SkillDisplayDto> skillDisplayDtoList = skillList
                .stream()
                .map(skill -> {
                    SkillDisplayDto skillDisplayDto = new SkillDisplayDto();
                    BeanUtils.copyProperties(skill, skillDisplayDto);
                    return skillDisplayDto;
                }).collect(Collectors.toList());

        model.addAttribute("skillDisplayDtoList", skillDisplayDtoList);
    }

    private void initBenefitList(Model model) { //Get all benefit form DB
        List<Benefit> benefitList = benefitService.findAll();
        List<BenefitDisplayDto> benefitDisplayDtoList = benefitList
                .stream()
                .map(benefit -> {
                    BenefitDisplayDto benefitDisplayDto = new BenefitDisplayDto();
                    BeanUtils.copyProperties(benefit, benefitDisplayDto);
                    return benefitDisplayDto;
                }).collect(Collectors.toList());

        model.addAttribute("benefitDisplayDtoList", benefitDisplayDtoList);
    }

    @GetMapping()
    public String showJobList(JobSearchParamDto searchParamDto, Model model) {
        Specification<Job> basicConditions = jobService.undeletedSpec();   //where 1. mac dinh

        if (StringUtils.hasLength(searchParamDto.getSearchKey())) {
            basicConditions = basicConditions.and(jobService.buildSpecForSearch(searchParamDto.getSearchKey()));  //where 2. dk khac
        }
        if (StringUtils.hasLength(searchParamDto.getStatus())
                && EnumUtils.isValidName(JobStatus.class, searchParamDto.getStatus())) {
            Specification<Job> statusCondition = (root, query, criteriaBuilder)
                    -> criteriaBuilder.equal(root.get("jobStatus"), JobStatus.valueOf(searchParamDto.getStatus().toUpperCase()));
            basicConditions = basicConditions.and(statusCondition);
        }
        //setup page with sort and search
        List<Sort.Order> orderList = buildSortByKey(searchParamDto.getSort());
        Page<Job> jobPage = jobService.findAll(
                basicConditions, PageRequest.of(searchParamDto.getPage(), searchParamDto.getSize(), Sort.by(orderList))
        );
        model.addAttribute("searchKey", searchParamDto.getSearchKey());
        model.addAttribute("status", searchParamDto.getStatus());
        if (jobPage.isEmpty()) {
            model.addAttribute("contentPage", Page.empty());
            return "/job/view-job-list";
        }

        List<ViewListJobManagerRecruiterDto> viewListJobManagerRecruiterDtoList = jobPage.getContent()
                .stream()
                .map(Job::convertToViewListJobManagerRecruiterDto)
                .collect(Collectors.toList());
        Page<ViewListJobManagerRecruiterDto> contentPage = new PageImpl<>(viewListJobManagerRecruiterDtoList,
                PageRequest.of(searchParamDto.getPage(), searchParamDto.getSize(), Sort.by(orderList)),
                jobPage.getTotalElements());
        model.addAttribute("contentPage", contentPage);
        model.addAttribute("sort", searchParamDto.getSort());
        return "/job/view-job-list";
    }

    @GetMapping("/{jobId}")
    public String viewJobDetail(@PathVariable(name = "jobId") Long id,
                                Model model) {
        Optional<Job> jobOptional = jobService.findById(id);
        if (jobOptional.isEmpty()) {
            throw new EntityNotFoundException("Cannot find job with id: " + id);
        }
        Job job = jobOptional.get();
        //create jobDisplayDto
        JobDisplayDto jobDisplayDto = convertToDisplayDto(job);
        //send attribute to view
        model.addAttribute("jobDisplayDto", jobDisplayDto);
        return "job/view-job-detail";
    }

    public JobDisplayDto convertToDisplayDto(Job job) {

        JobDisplayDto jobDisplayDto = new JobDisplayDto();
        BeanUtils.copyProperties(job, jobDisplayDto);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(CommonConstants.FORMAT_DATE);

        //set requiredSkillSet
        jobDisplayDto.setRequiredSkillSet(
                job.getRequiredSkillSet()
                        .stream()
                        .map(jobSkill -> jobSkill.getSkill().getSkillName())
                        .collect(Collectors.toList())
        );
        //set startDate
        jobDisplayDto.setStartDate(job.getStartDate().format(dtf));
        //set endDate
        jobDisplayDto.setEndDate(job.getEndDate().format(dtf));
        //set salaryFrom
        jobDisplayDto.setSalaryFrom(job.getSalaryFrom().toString());
        //set salaryTo
        jobDisplayDto.setSalaryTo(job.getSalaryTo().toString());
        //set jobBenefitSet
        jobDisplayDto.setJobBenefitSet(
                job.getJobBenefitSet()
                        .stream()
                        .map(jobBenefit -> jobBenefit.getBenefit().getBenefit())
                        .collect(Collectors.toList())
        );

        return jobDisplayDto;
    }

    @GetMapping("/edit/{id}")
    public String showEditJob(@PathVariable Long id, Model model) {
        Job job = jobService.findById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find Job with id " + id));
        JobEditParamDto jobParamDto = new JobEditParamDto();
        //convert job to jobParamDto
        BeanUtils.copyProperties(job, jobParamDto);
        //set list Skill id
        if (!job.getRequiredSkillSet().isEmpty())
            jobParamDto.setSkillIdList(
                    job.getRequiredSkillSet()
                            .stream()
                            .map(JobSkill::getSkill)
                            .collect(Collectors.toUnmodifiableList())
                            .stream()
                            .map(Skill::getId)
                            .collect(Collectors.toUnmodifiableList())
                            .toArray(new Long[]{})
            );
        //set list benefit id
        if (!job.getJobBenefitSet().isEmpty())
            jobParamDto.setBenefitIdList(
                    job.getJobBenefitSet()
                            .stream()
                            .map(JobBenefit::getBenefit)
                            .collect(Collectors.toUnmodifiableList())
                            .stream()
                            .map(Benefit::getId)
                            .collect(Collectors.toUnmodifiableList())
                            .toArray(new Long[]{})
            );
        //set jobLevel array
        jobParamDto.setJobLevel(Arrays.stream(job.getJobLevel().split(", ")).map(
                jobLevelString -> JobLevel.valueOf(jobLevelString.toUpperCase())
        ).collect(Collectors.toList()).toArray(JobLevel[]::new)
        );

        model.addAttribute("jobParamDto", jobParamDto);
        //transmit skill list & benefit list to view
        initSkillList(model);
        initBenefitList(model);
        return "/job/create-job";
    }

    @PostMapping("/edit/{id}")
    public String editJob(@PathVariable(name = "id") Long id,
                          @ModelAttribute(name = "jobParamDto") @Valid JobEditParamDto jobParamDto,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        Job job = jobService.findById(id).orElseThrow(() -> new EntityNotFoundException("Can not find Job with id " + id));
        if (!bindingResult.hasErrors()) {
            if (jobParamDto.getSalaryFrom() == null) {
                jobParamDto.setSalaryFrom(BigDecimal.valueOf(0));
            }
            if (jobParamDto.getSalaryFrom().compareTo(jobParamDto.getSalaryTo()) > 0) {
                bindingResult.rejectValue("salaryTo", "Message.097");
            }
            if (jobParamDto.getStartDate().isAfter(jobParamDto.getEndDate())) {
                bindingResult.rejectValue("endDate", "Message.096");
            }
            //check job status
            if (jobParamDto.getJobStatus().equals(JobStatus.CLOSED)){
                if (job.getInterviewSchedule() != null && job.getInterviewSchedule()
                        .stream()
                        .anyMatch(interviewSchedule -> interviewSchedule != null &&
                                (interviewSchedule.getInterviewStatus().equals(InterviewScheduleStatus.OPEN) ||
                                interviewSchedule.getInterviewStatus().equals(InterviewScheduleStatus.INVITED)))
                ) bindingResult.rejectValue("jobStatus", "Message.023");
            }
        }
        if (bindingResult.hasErrors()) {
            initSkillList(model);
            initBenefitList(model);
            return "job/create-job";
        }
        job.getRequiredSkillSet().forEach(jobSkill -> jobSkill.setJob(null) );
        //convert to Job
        BeanUtils.copyProperties(jobParamDto, job, "jobLevel");
        job.setRequiredSkillSet(
                Arrays.stream(jobParamDto.getSkillIdList())
                        .map(skillId -> new JobSkill(Skill.builder().id(skillId).build(), job))
                        .collect(Collectors.toSet())
        );
        job.getJobBenefitSet().forEach(jobBenefit -> jobBenefit.setJob(null));
        job.setJobBenefitSet(Arrays.stream(jobParamDto.getBenefitIdList())
                .map(benefitId -> new JobBenefit(Benefit.builder().id(benefitId).build(), job))
                .collect(Collectors.toSet()));
        //update
        try {
            jobService.update(job);
            redirectAttributes.addFlashAttribute("messageSuccess", "Message.087");
            return "redirect:/jobs";
        } catch (Exception e) {
            e.printStackTrace();
            initSkillList(model);
            initBenefitList(model);
            model.addAttribute("messageFail", "Message.086");
            return "/job/create-job";
        }
    }


    @PostMapping("/import")
    public String importFile(@RequestParam("file") MultipartFile excelFile,
                             RedirectAttributes redirectAttributes) throws IOException {

        List<Job> jobList = new LinkedList<>();
        Workbook workbook = new XSSFWorkbook(excelFile.getInputStream());
        Sheet sheet = workbook.getSheet("Job");

        try {
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                Job job = new Job();

                //Set Job title:
                job.setJobTitle(row.getCell(1).getStringCellValue());

                //Set Job Skill
                String[] skillNameList = row.getCell(2).getStringCellValue().split(",");
                Long[] skillIdList = Arrays.stream(skillNameList).map(skillName -> {
                    Optional<Skill> optionalSkill = skillService.findBySkillName(skillName);
                    if (optionalSkill.isEmpty()) {
                        throw new EntityNotFoundException();
                    }
                    return optionalSkill.get().getId();
                }).collect(Collectors.toList()).toArray(Long[]::new);

                Set<JobSkill> jobSkillSet = Arrays.stream(skillIdList)
                        .map(skillId -> new JobSkill(Skill.builder().id(skillId).build(), job))
                        .collect(Collectors.toSet());
                job.setRequiredSkillSet(jobSkillSet);

                LocalDate startDate = row.getCell(3)
                        .getDateCellValue()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                LocalDate endDate = row.getCell(4)
                        .getDateCellValue()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                if (startDate.isAfter(endDate)){
                    redirectAttributes.addFlashAttribute("messageFail","Message.088");
                    return "redirect:/jobs";
                }

                //Set Job Start Date
                job.setStartDate(startDate);
                //Set Job End Date
                job.setEndDate(endDate);

                //Set SalaryForm
                job.setSalaryFrom(BigDecimal.valueOf(row.getCell(5).getNumericCellValue()));

                //Set SalaryForm
                job.setSalaryTo(BigDecimal.valueOf(row.getCell(6).getNumericCellValue()));

                //Set Benefit
                String[] benefitNameList = row.getCell(7).getStringCellValue().split(",");
                Long[] benefitIdList = Arrays.stream(benefitNameList).map(benefitName -> {
                    Benefit benefit = benefitService.findByBenefit(benefitName)
                            .orElseThrow(EntityNotFoundException::new);
                    return benefit.getId();
                }).collect(Collectors.toList()).toArray(Long[]::new);

                Set<JobBenefit> jobBenefitSet = Arrays.stream(benefitIdList)
                        .map(benefitId -> new JobBenefit(Benefit.builder().id(benefitId).build(), job))
                        .collect(Collectors.toSet());
                job.setJobBenefitSet(jobBenefitSet);

                //Set Working Address
                job.setWorkingAddress(row.getCell(8).getStringCellValue());

                //Validate Level
                String[] importJobLevel = Arrays.stream(row.getCell(9).getStringCellValue().split(","))
                        .map(s -> StringUtils.capitalize(s.toLowerCase()))
                        .collect(Collectors.toList()).toArray(String[]::new);

                String[] values = Arrays.stream(JobLevel.values())
                        .map(JobLevel::getDisplayValue)
                        .collect(Collectors.toList()).toArray(String[]::new);
                String listJobLevel = String.join(",", values);

                try{
                    for (String s : importJobLevel) {
                        if (!listJobLevel.contains(s)){
                            throw new IllegalArgumentException();
                        }
                    }
                    job.setJobLevel(String.join(", ", importJobLevel));
                } catch (Exception e){
                    redirectAttributes.addFlashAttribute("messageFail","Message.088");
                    return "redirect:/jobs";
                }

                //Set Description
                job.setDescription(row.getCell(10).getStringCellValue());

                //Add job to Job List
                jobList.add(job);
            }
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("messageFail", "Message.088");
            return "redirect:/jobs";
        }

        //Add job
        try {
            jobList.forEach(jobService::createNew);
            redirectAttributes.addFlashAttribute("messageSuccess", "Message.089");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("messageFail", "Message.088");
        }

        return "redirect:/jobs";
    }
}