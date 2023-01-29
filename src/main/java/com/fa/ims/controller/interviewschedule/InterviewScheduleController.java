package com.fa.ims.controller.interviewschedule;
import com.fa.ims.controller.BaseController;
import com.fa.ims.dto.InterviewScheduleParamDto;
import com.fa.ims.dto.ViewListInterviewScheduleDto;
import com.fa.ims.entity.Candidate;
import com.fa.ims.entity.InterviewSchedule;
import com.fa.ims.dto.*;
import com.fa.ims.entity.*;
import com.fa.ims.enums.CandidateStatus;
import com.fa.ims.enums.InterviewResult;
import com.fa.ims.enums.InterviewScheduleStatus;
import com.fa.ims.enums.UserRole;
import com.fa.ims.security.SecurityUtil;
import com.fa.ims.service.CandidateService;
import com.fa.ims.service.InterviewScheduleService;
import com.fa.ims.service.JobService;
import com.fa.ims.service.UserService;
import com.fa.ims.util.EnumUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/interviews")

public class InterviewScheduleController extends BaseController {

    private final UserService userService;
    private final JobService jobService;
    private final CandidateService candidateService;
    private final InterviewScheduleService interviewScheduleService;

    @GetMapping("/create")
    public String showCreateInterviewSchedule(Model model){
        InterviewScheduleParamDto interviewScheduleParamDto = new InterviewScheduleParamDto();
        model.addAttribute("interviewScheduleParamDto", interviewScheduleParamDto);
        callInitCandidateInterviewerRecruiter(model);
        return "interviewschedule/create-interview-schedule";
    }
    private void initInterviewersList(Model model) {
        List<InterviewerDisplayDto> interviewerDisplayDtoList = userService.findAllInterviewer()
                .stream().map(user -> {
                    InterviewerDisplayDto interviewerDisplayDto = new InterviewerDisplayDto();
                    BeanUtils.copyProperties(user, interviewerDisplayDto);
                    return interviewerDisplayDto;
                }).collect(Collectors.toList());
        model.addAttribute("interviewerDisplayDto", interviewerDisplayDtoList);
    }
    private void initRecruiterList(Model model) {
        List<RecruiterDisplayDto> recruiterDisplayDtoList = userService.findAllRecruiter().stream().map(user -> {
            RecruiterDisplayDto recruiterDisplayDto = new RecruiterDisplayDto();
            BeanUtils.copyProperties(user, recruiterDisplayDto);
            return recruiterDisplayDto;
        }).collect(Collectors.toList());
        model.addAttribute("recruiterDisplayDto", recruiterDisplayDtoList);
    }
    private void initCandidateList(Model model){
        List<CandidateDisplayInterviewDto> candidateDisplayInterviewDtos = candidateService.findAll().stream().map(candidate -> {
            CandidateDisplayInterviewDto candidateDisplayInterviewDto = new CandidateDisplayInterviewDto();
            BeanUtils.copyProperties(candidate, candidateDisplayInterviewDto);
            return candidateDisplayInterviewDto;
        }).collect(Collectors.toList());
        model.addAttribute("candidateDisplayInterviewDto", candidateDisplayInterviewDtos);
    }
    private void initJobList(Model model){
        List<JobDisplayDto> jobDisplayDtoList = jobService.findAll().stream().map(job -> {
            JobDisplayDto jobDisplayDto = new JobDisplayDto();
            BeanUtils.copyProperties(job, jobDisplayDto);
            return jobDisplayDto;
        }).collect(Collectors.toList());
        model.addAttribute("jobDisplayDtoList", jobDisplayDtoList);
    }
    private void callInitCandidateInterviewerRecruiter(Model model){
        initCandidateList(model);
        initInterviewersList(model);
        initRecruiterList(model);
        initJobList(model);
    };

    @PostMapping("/create")
    public String createInterviewSchedule(@ModelAttribute @Valid InterviewScheduleParamDto interviewParamDto,
                                          BindingResult bindingResult,
                                          Model model,
                                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            callInitCandidateInterviewerRecruiter(model);
            return "interviewschedule/create-interview-schedule";
        }
        InterviewSchedule interviewSchedule = new InterviewSchedule();
        BeanUtils.copyProperties(interviewParamDto, interviewSchedule);

        //Set Schedule Time From, Time To
        LocalDateTime scheduleTimeFrom = LocalDateTime.of(interviewParamDto.getDisplayDate(),
                interviewParamDto.getScheduleTimeFrom().toLocalTime());
        LocalDateTime scheduleTimeTo = LocalDateTime.of(interviewParamDto.getDisplayDate(),
                interviewParamDto.getScheduleTimeTo().toLocalTime());

        boolean isValid = false;
        if (!scheduleTimeFrom.isBefore(scheduleTimeTo)){
            bindingResult.rejectValue("scheduleTimeFrom", "Message.099");
            isValid = true;
        } else if (scheduleTimeFrom.isBefore(LocalDateTime.now()) || scheduleTimeTo.isBefore(LocalDateTime.now())){
            bindingResult.rejectValue("scheduleTimeFrom", "Message.098");
            isValid = true;
        }
        if (isValid){
            callInitCandidateInterviewerRecruiter(model);
            return "interviewschedule/create-interview-schedule";
        }

        //Set Time
        interviewSchedule.setScheduleTimeFrom(scheduleTimeFrom);
        interviewSchedule.setScheduleTimeTo(scheduleTimeTo);

        //Set Candidate
        Candidate candidate = candidateService.findById(interviewParamDto.getCandidateId())
                .orElseThrow(() -> new EntityNotFoundException("Cannot find Candidate with id" + interviewParamDto.getId()));
        interviewSchedule.setCandidate(candidate);

        //Set Inter View Schedule User Interview
        if (interviewParamDto.getInterviewerId() != null) {
            Set<InterviewScheduleUser> interviewScheduleUserSet = Arrays
                    .stream(interviewParamDto.getInterviewerId())
                    .map(interviewId -> new InterviewScheduleUser(User.builder().id(interviewId).build(), interviewSchedule))
                    .collect(Collectors.toSet());
            interviewSchedule.setInterviewerSet(interviewScheduleUserSet);
        }

        //Set Recruiter
        User user = userService.findById(interviewParamDto.getRecruiterOwnerId())
                .orElseThrow(() -> new EntityNotFoundException("Cannot find Interviewer with id" + interviewParamDto.getRecruiterOwnerId()));
        interviewSchedule.setRecruiterOwner(user);
        try {
            interviewScheduleService.createNew(interviewSchedule);
            redirectAttributes.addFlashAttribute("messageSuccess", "Message.093");
            return "redirect:/interviews";
        } catch (Exception e) {
            callInitCandidateInterviewerRecruiter(model);
            model.addAttribute("messageFail", "Message.092");
            return "interviewschedule/create-interview-schedule";
        }
    }


    @GetMapping()
    public String showInterviewSchedule(JobSearchParamDto jobSearchParamDto,Model model) {
        //setup Search condition
        Specification<InterviewSchedule> basicConditions = interviewScheduleService.undeletedSpec();
        //restrict Interviewer access
        if (SecurityUtil.getListUserLoginRole()
                .stream()
                .anyMatch(userRole -> userRole.equals(UserRole.ROLE_INTERVIEWER))
        ){
            Specification<InterviewSchedule> userRoleCondition = (root, query, criteriaBuilder) -> {
                String userName = SecurityUtil.getCurrentUserLogin()
                        .orElseThrow(()->new EntityNotFoundException("User Login can not be found"));
                Join<InterviewSchedule, InterviewScheduleUser> join01 = root.join("interviewerSet", JoinType.LEFT);
                Join<InterviewScheduleUser,User> interviewerJoin = join01.join("user",JoinType.LEFT);
                query.distinct(true);
                return criteriaBuilder.equal(interviewerJoin.get("username"), userName);
            };
            basicConditions = basicConditions.and(userRoleCondition);
        }

        if (StringUtils.hasLength(jobSearchParamDto.getSearchKey())) {
            basicConditions = basicConditions.and(interviewScheduleService.buildSpecForSearch(jobSearchParamDto.getSearchKey()));
        }
        if (StringUtils.hasLength(jobSearchParamDto.getStatus())
                && EnumUtils.isValidName(InterviewScheduleStatus.class, jobSearchParamDto.getStatus())) {
            InterviewScheduleStatus status = InterviewScheduleStatus.valueOf(jobSearchParamDto.getStatus().toUpperCase());
            Specification<InterviewSchedule> statusCondition = (root, query, criteriaBuilder)
                    -> criteriaBuilder.equal(root.get("interviewStatus"), status);
            basicConditions = basicConditions.and(statusCondition);
        }
        //setup sort order
        jobSearchParamDto.setSort("interviewStatus,-scheduleTimeTo,-lastModifiedDate");
        List<Sort.Order> orderList = buildSortByKey(jobSearchParamDto.getSort());
        //setup page with sort and search
        Page<InterviewSchedule> interviewSchedulePage = interviewScheduleService.findAll(
                basicConditions, PageRequest.of(jobSearchParamDto.getPage(), jobSearchParamDto.getSize(), Sort.by(orderList))
        );
        model.addAttribute("searchKey", jobSearchParamDto.getSearchKey());
        model.addAttribute("status", jobSearchParamDto.getStatus());
        if (interviewSchedulePage.isEmpty()){
            model.addAttribute("contentPage", Page.empty());
            return "/interviewschedule/interview-schedule-list";
        }
        //get all content binding with these roles
        List<ViewListInterviewScheduleDto> viewListInterviewScheduleDtoList =
                interviewSchedulePage.getContent()
                        .stream()
                        .map(this::convertToViewListInterviewScheduleDto)
                        .collect(Collectors.toList());
        Page<ViewListInterviewScheduleDto> contentPage = new PageImpl<>(
                viewListInterviewScheduleDtoList, PageRequest.of(jobSearchParamDto.getPage(),jobSearchParamDto.getSize(),Sort.by(orderList))
                ,interviewSchedulePage.getTotalElements()
        );
        model.addAttribute("contentPage", contentPage);
        model.addAttribute("sort", jobSearchParamDto.getSort());
        return "/interviewschedule/interview-schedule-list";
    }

    @GetMapping("/edit/{id}")
    public String showEditInterviewSchedule (@PathVariable(name = "id") Long id, Model model){
        InterviewSchedule interviewSchedule = interviewScheduleService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can not find Interview Schedule with id "+id));
        InterviewScheduleParamDto interviewScheduleParamDto = new InterviewScheduleParamDto();
        BeanUtils.copyProperties(interviewSchedule, interviewScheduleParamDto);
        //convert Long candidateId
        interviewScheduleParamDto.setCandidateId(
                interviewSchedule.getCandidate().getId()
        );
        //convert Long[] interviewerId
        interviewScheduleParamDto.setInterviewerId(
                interviewSchedule.getInterviewerSet()
                        .stream()
                        .map(interviewScheduleUser -> interviewScheduleUser.getUser().getId())
                        .toArray(Long[]::new)
        );
        //convert Long recruiterOwnerId
        interviewScheduleParamDto.setRecruiterOwnerId(
                interviewSchedule.getRecruiterOwner().getId()
        );

        interviewScheduleParamDto.setDisplayDate(interviewSchedule.getScheduleTimeFrom().toLocalDate());
        model.addAttribute("interviewScheduleParamDto",interviewScheduleParamDto);
        callInitCandidateInterviewerRecruiter(model);
        return "/interviewschedule/edit-interview-schedule";
    }

    @PostMapping("/edit/{id}")
    public String editInterviewSchedule (@PathVariable(name = "id") Long id,
                                         @ModelAttribute @Valid InterviewScheduleParamDto interviewScheduleParamDto,
                                         BindingResult bindingResult,
                                         Model model,
                                         RedirectAttributes redirectAttributes){
        InterviewSchedule interviewSchedule = interviewScheduleService
                .findById(id).orElseThrow(() -> new EntityNotFoundException("Can not find Job with id " + id));

        if (!bindingResult.hasErrors()) {
            if (!interviewScheduleParamDto.getScheduleTimeFrom().isBefore(interviewScheduleParamDto.getScheduleTimeTo())){
                bindingResult.rejectValue("scheduleTimeFrom", "Message.099");

            }

            if (interviewSchedule.getInterviewStatus().equals(InterviewScheduleStatus.INTERVIEWED) &&
                    !interviewScheduleParamDto.getInterviewStatus().equals(InterviewScheduleStatus.INTERVIEWED)) {
                bindingResult.rejectValue("interviewStatus", "Message.024");
                interviewScheduleParamDto.setInterviewStatus(InterviewScheduleStatus.INTERVIEWED);

            }
        }

        if (bindingResult.hasErrors()) {
            callInitCandidateInterviewerRecruiter(model);
            return "interviewschedule/edit-interview-schedule";
        }

        BeanUtils.copyProperties(interviewScheduleParamDto, interviewSchedule);
        //convert Long candidateId
        interviewSchedule.setCandidate(
                candidateService.findById(interviewScheduleParamDto.getCandidateId())
                        .orElseThrow(()->new EntityNotFoundException("Can not find Candidate with Id "+
                                interviewScheduleParamDto.getCandidateId()))
        );
        interviewSchedule.getInterviewerSet().forEach(interviewScheduleUser -> interviewScheduleUser.setInterviewerSet(null));
        //convert Long[] interviewerId
        interviewSchedule.setInterviewerSet(
                Arrays.stream(interviewScheduleParamDto.getInterviewerId())
                        .map(interviewScheduleUserId-> new InterviewScheduleUser(User.builder()
                                .id(interviewScheduleUserId)
                                .build()
                                ,interviewSchedule))
                        .collect(Collectors.toSet())
        );

        //convert Long recruiterOwnerId
        interviewSchedule.setRecruiterOwner(
                userService.findById(interviewScheduleParamDto.getRecruiterOwnerId())
                        .orElseThrow(()->new EntityNotFoundException("Can not find User with Id "+
                                interviewScheduleParamDto.getRecruiterOwnerId()) )
        );

        interviewScheduleService.update(interviewSchedule);
        redirectAttributes.addFlashAttribute("messageUpdateSuccess", "Message.022");
        return "redirect:/interviews";
    }
    @GetMapping("/{interviewId}")
    public String viewInterviewDetail(@PathVariable(name = "interviewId") Long id,
                                      Model model) {

        InterviewSchedule interviewSchedule = interviewScheduleService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find interview with id: " + id));

        InterviewScheduleDisplayDto interviewScheduleDisplayDto = convertToDisplayDto(interviewSchedule);

        if (interviewSchedule.getCandidate().getAttachFile() != null) {
            String relativePath = interviewScheduleDisplayDto.getCandidate().getAttachFile();
            interviewScheduleDisplayDto.getCandidate().setAttachFile(relativePath.replace("\\", "/"));
        }

        model.addAttribute("interviewDisplayDto", interviewScheduleDisplayDto);

        return "interviewschedule/view-interview-schedule-details";
    }

    @PostMapping("/submit/{id}")
    public String submitInterviewResult(@PathVariable Long id,
                                        @ModelAttribute @Valid InterviewScheduleDisplayDto interviewScheduleDisplayDto,
                                        BindingResult bindingResult,
                                        RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()){
            return "interviewschedule/view-interview-schedule-details";
        }

        InterviewSchedule interviewSchedule = interviewScheduleService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found interview schedule with id " + id));

        //Set Interview Note
        interviewSchedule.setNote(interviewScheduleDisplayDto.getNote());

        //Check and set Interview Result and Candidate Status
        if (interviewScheduleDisplayDto.getInterviewResult() == null){
            interviewSchedule.setNote(interviewScheduleDisplayDto.getNote());
            interviewScheduleService.update(interviewSchedule);
            return "interviewschedule/view-interview-schedule-details";
        }

        try{
            InterviewResult result = interviewScheduleDisplayDto.getInterviewResult();
            if (result.equals(InterviewResult.PASS)){
                interviewSchedule.getCandidate().setCandidateStatus(CandidateStatus.PASSED_INTERVIEW);
            }
            if (result.equals(InterviewResult.FAIL)){
                interviewSchedule.getCandidate().setCandidateStatus(CandidateStatus.FAILED_INTERVIEW);
            }
            interviewSchedule.setInterviewResult(result);
            interviewSchedule.setInterviewStatus(InterviewScheduleStatus.INTERVIEWED);
        } catch (Exception e){
            throw new IllegalArgumentException("Invalid interview result!");
        }

        try {
            interviewScheduleService.update(interviewSchedule);
            redirectAttributes.addFlashAttribute("messageSuccess", "Message.083");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("messageFail", "Message.082");
        }

        return "redirect:/interviews";
    }

    public InterviewScheduleDisplayDto convertToDisplayDto(InterviewSchedule interviewSchedule) {

        InterviewScheduleDisplayDto interviewScheduleDisplayDto = new InterviewScheduleDisplayDto();

        BeanUtils.copyProperties(interviewSchedule, interviewScheduleDisplayDto);

        //Convert CandidateName
        interviewScheduleDisplayDto.setCandidate(interviewSchedule.getCandidate());

        //Convert Set Interviewer
        interviewScheduleDisplayDto.setInterviewerSet(
                interviewSchedule.getInterviewerSet().stream()
                        .map(InterviewScheduleUser::getUser)
                        .collect(Collectors.toList())
        );

        //Convert Note
        if (interviewSchedule.getNote() == null || interviewSchedule.getNote().isEmpty()){
            interviewScheduleDisplayDto.setNote("N/A");
        } else {
            interviewScheduleDisplayDto.setNote(interviewSchedule.getNote());
        }

        //Convert Result
        interviewScheduleDisplayDto.setInterviewResult(interviewSchedule.getInterviewResult());

        //Convert Status
        interviewScheduleDisplayDto.setInterviewStatus(interviewSchedule.getInterviewStatus());

        // Convert recruiter owner
        interviewScheduleDisplayDto.setRecruiterOwner(interviewSchedule.getRecruiterOwner());

        //Convert Job
        if (interviewSchedule.getJob() != null){
            interviewScheduleDisplayDto.setJobTitle(interviewSchedule.getJob().getJobTitle());
        }

        //Convert Position
        if (interviewSchedule.getPosition() != null){
            interviewScheduleDisplayDto.setCandidatePosition(interviewSchedule.getPosition().getDisplayValue());
        }

        return interviewScheduleDisplayDto;
    }

    public ViewListInterviewScheduleDto convertToViewListInterviewScheduleDto (InterviewSchedule interviewSchedule){
        ViewListInterviewScheduleDto viewListInterviewScheduleDto = new ViewListInterviewScheduleDto();
        BeanUtils.copyProperties(interviewSchedule,viewListInterviewScheduleDto);
        Optional<Candidate> candidate = candidateService.findById(interviewSchedule.getCandidate().getId());
        viewListInterviewScheduleDto.setCandidateName(
                candidate.isEmpty()? null : candidate.get().getFullName()
        );
        viewListInterviewScheduleDto.setJobTitle(
                interviewSchedule.getJob() != null ? interviewSchedule.getJob().getJobTitle() : null
        );
        //merge status column with result column
        viewListInterviewScheduleDto.setInterviewStatus(
                interviewSchedule.getInterviewResult() != null ? String.join(
                        " - ",
                        interviewSchedule.getInterviewStatus().getDisplayValue(),
                        interviewSchedule.getInterviewResult().getDisplayValue()) : interviewSchedule.getInterviewStatus().getDisplayValue()
        );
        return viewListInterviewScheduleDto;
    }

}