package com.fa.ims.controller;

import com.fa.ims.dto.*;
import com.fa.ims.entity.Candidate;
import com.fa.ims.entity.CandidateSkill;
import com.fa.ims.entity.Skill;
import com.fa.ims.entity.User;
import com.fa.ims.enums.CandidateStatus;
import com.fa.ims.enums.UserRole;
import com.fa.ims.security.SecurityUtil;
import com.fa.ims.service.*;
import com.fa.ims.util.EnumUtils;

import org.springframework.beans.BeanUtils;

import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAmount;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/candidates")
public class CandidateController extends BaseController {
    SkillService skillService;
    UserService userService;
    FileStorageService fileStorageService;
    CandidateService candidateService;
    CandidateSkillService candidateSkillService;

    @Value("${app.file.location.cv}")
    String fileLocation;

    public CandidateController(SkillService skillService, UserService userService, FileStorageService fileStorageService, CandidateService candidateService, CandidateSkillService candidateSkillService) {
        this.skillService = skillService;
        this.userService = userService;
        this.fileStorageService = fileStorageService;
        this.candidateService = candidateService;
        this.candidateSkillService = candidateSkillService;
    }

    @GetMapping("/{candidateId}")
    public String showViewCandidate(@PathVariable(name = "candidateId") Long id, Model model) {
        Candidate candidate = candidateService.findById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find Candidate with id" + id));

        CandidateDisplayDto candidateDisplayDto = new CandidateDisplayDto();

        BeanUtils.copyProperties(candidate, candidateDisplayDto);
        if (candidateDisplayDto.getAttachFile() != null) {
            String relativePath = candidateDisplayDto.getAttachFile();
            candidateDisplayDto.setAttachFile(relativePath.replace("\\", "/"));
        }

        model.addAttribute("candidateDisplayDto", candidateDisplayDto);
        return "candidate/view-candidate";
    }

    @GetMapping("/create")
    public String showAddCandidate(Model model) {
        CandidateCreateFormDto candidateCreateFormDto = new CandidateCreateFormDto();
        model.addAttribute("skillList", getSkillDisplayDto());
        model.addAttribute("recruiterList", getRecruiterListDisplayDto());
        model.addAttribute("candidateCreateFormDto", candidateCreateFormDto);
        return "candidate/create-candidate";
    }

    @PostMapping("/create")
    public String addCandidate(@ModelAttribute("candidateCreateFormDto") @Valid CandidateCreateFormDto candidateCreateFormDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) throws IOException {
        if (candidateService.existEmail(candidateCreateFormDto.getEmail())) {
            bindingResult.rejectValue("email", "message.0012", "Email is existing");
        }
        if (candidateService.existPhone(candidateCreateFormDto.getPhone())) {
            bindingResult.rejectValue("phone", "message.0015", "Phone is existing");
        }

        if (!candidateCreateFormDto.getAttachFile().isEmpty()) {
            isFileUploadInValid(candidateCreateFormDto.getAttachFile(), bindingResult);
        }
        if (isDateOfBirthInvalid(candidateCreateFormDto.getDob())) {
            bindingResult.rejectValue("dob", "message.0021", "Age must be lager than 18");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("skillList", getSkillDisplayDto());
            model.addAttribute("recruiterList", getRecruiterListDisplayDto());
            return "candidate/create-candidate";
        }

        Candidate newCandidate = new Candidate();
        BeanUtils.copyProperties(candidateCreateFormDto, newCandidate, "attachFile", "candidateSkillList", "recruiterId");

        if (candidateCreateFormDto.getCandidateSkillList() != null) {
            newCandidate.setCandidateSkillSet(convertSkillIdToCandidateSkill(newCandidate, candidateCreateFormDto.getCandidateSkillList()));
        }

        newCandidate.setRecruiter(User.builder().id(candidateCreateFormDto.getRecruiterId()).build());

        if (!candidateCreateFormDto.getAttachFile().isEmpty()) {
            String relativeFilePath = fileStorageService.saveFile(candidateCreateFormDto.getAttachFile(), candidateCreateFormDto.getPhone());
            newCandidate.setAttachFile(relativeFilePath);
        }

        try {
            candidateService.createNew(newCandidate);
            redirectAttributes.addFlashAttribute("messageSuccess", "message.006");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("messageFail", "message.005");
        }
        return "redirect:/candidates";
    }

    @GetMapping("/edit/{candidateId}")
    public String showUpdateCandidate(@PathVariable(name = "candidateId") Long id, Model model) throws IOException {
        Candidate candidate = candidateService.findById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find Candidate with id" + id));

        CandidateParamDto candidateParamDto = new CandidateParamDto();
        BeanUtils.copyProperties(candidate, candidateParamDto);

        if (candidate.getCandidateSkillSet() != null) {
            List<Long> skillIdList = candidate.getCandidateSkillSet().stream().map(sk -> sk.getSkill().getId()).collect(Collectors.toList());
            candidateParamDto.setCandidateSkillList(skillIdList.toArray(new Long[]{}));
        }

        if (candidate.getAttachFile() != null) {
            candidateParamDto.setAttachFile(candidate.getAttachFile().replace("\\", "/"));
        }
        candidateParamDto.setRecruiterId(candidate.getRecruiter().getId());

        model.addAttribute("candidateParamDto", candidateParamDto);
        model.addAttribute("skillDtoList", getSkillDisplayDto());
        model.addAttribute("recruiterList", getRecruiterListDisplayDto());

        return "candidate/candidate-update";
    }

    @Transactional
    @PostMapping("/edit/{candidateId}")
    public String updateCandidate(@PathVariable(name = "candidateId") Long id, @ModelAttribute("candidateParamDto") @Valid CandidateParamDto candidateParamDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) throws IOException {

        Candidate candidate = candidateService.findById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find Candidate with id" + id));
        if (isDateOfBirthInvalid(candidateParamDto.getDob())) {
            bindingResult.rejectValue("dob", "message.0021", "Age must be lager than 18");
        }
        if (!candidateParamDto.getFileUpdate().isEmpty()) {
            isFileUploadInValid(candidateParamDto.getFileUpdate(), bindingResult);
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("skillDtoList", getSkillDisplayDto());
            model.addAttribute("recruiterList", getRecruiterListDisplayDto());
            return "candidate/candidate-update";
        }
        if (isDateOfBirthInvalid(candidateParamDto.getDob())) {
            bindingResult.rejectValue("dob", "message.0021", "Age must be lager than 18");
        }
        BeanUtils.copyProperties(candidateParamDto, candidate, "attachFile", "candidateSkillList", "recruiterId", "phone", "email");

        candidate.setRecruiter(User.builder().id(candidateParamDto.getRecruiterId()).build());

        candidateSkillService.deleteByCandidateId(candidate.getId());
        if (candidateParamDto.getCandidateSkillList() != null) {
            candidate.setCandidateSkillSet(convertSkillIdToCandidateSkill(candidate, candidateParamDto.getCandidateSkillList()));
        }

        if (!candidateParamDto.getFileUpdate().isEmpty()) {
            String relativeFilePath = fileStorageService.saveFile(candidateParamDto.getFileUpdate(), candidateParamDto.getPhone());
            candidate.setAttachFile(relativeFilePath);
        }

        try {
            candidateService.update(candidate);
            redirectAttributes.addFlashAttribute("messageSuccess", "message.008");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("messageFail", "message.007");
        }
        return "redirect:/candidates";
    }

    @GetMapping
    public String showCandidateList(CandidateSearchParamDto searchParamDto, Model model) {

        Specification<Candidate> basicConditions = candidateService.undeletedSpec();

        //Interviewer chỉ xem được những candidate mà mình interview
        if (SecurityUtil.getListUserLoginRole().get(0) == UserRole.ROLE_INTERVIEWER) {
            String currentUsernameLogin = SecurityUtil.getCurrentUserLogin().orElseThrow(EntityNotFoundException::new);
            basicConditions = basicConditions.and(candidateService.filterInterviewerView(userService.findIdByUsername(currentUsernameLogin)));
        }

        if (StringUtils.hasLength(searchParamDto.getSearchKey())) {
            basicConditions = basicConditions.and(candidateService.buildSpecForSearch(searchParamDto.getSearchKey()));
        }

        if (StringUtils.hasLength(searchParamDto.getStatus())
                && EnumUtils.isValidName(CandidateStatus.class, searchParamDto.getStatus())) {
            Specification<Candidate> statusCondition = (root, query, criteriaBuilder)
                    -> criteriaBuilder.equal(root.get("candidateStatus"), CandidateStatus.valueOf(searchParamDto.getStatus().toUpperCase()));
            basicConditions = basicConditions.and(statusCondition);
        }

        List<Sort.Order> orderList = buildSortByKey(searchParamDto.getSort());
        Page<Candidate> candidatePage = candidateService.findAll(basicConditions,
                PageRequest.of(searchParamDto.getPage(), searchParamDto.getSize(), Sort.by(orderList)));

        model.addAttribute("searchKey", searchParamDto.getSearchKey());
        model.addAttribute("status", searchParamDto.getStatus());

        if (candidatePage.isEmpty()) {
            model.addAttribute("contentPage", Page.empty());
            return "candidate/list-candidate";
        }
        List<CandidateDisplayFormDto> candidateDisplayFormDtoList = candidatePage.getContent()
                .stream().map(Candidate::convertToDisplayDto).collect(Collectors.toList());

        Page<CandidateDisplayFormDto> contentPage = new PageImpl<>(candidateDisplayFormDtoList,
                PageRequest.of(searchParamDto.getPage(), searchParamDto.getSize(), Sort.by(orderList)),
                candidatePage.getTotalElements());

        model.addAttribute("contentPage", contentPage);
        model.addAttribute("sort", searchParamDto.getSort());
        return "candidate/list-candidate";
    }

    private List<SkillDisplayDto> getSkillDisplayDto() {
        return skillService.findAll().stream().map(skill -> {
            SkillDisplayDto skillDisplayDto = new SkillDisplayDto();
            BeanUtils.copyProperties(skill, skillDisplayDto);
            return skillDisplayDto;
        }).collect(Collectors.toList());
    }

    private List<RecruiterDisplayDto> getRecruiterListDisplayDto() {
        return userService.findAllRecruiter().stream().map(user -> {
            RecruiterDisplayDto recruiterDisplayDto = new RecruiterDisplayDto();
            BeanUtils.copyProperties(user, recruiterDisplayDto);
            return recruiterDisplayDto;
        }).collect(Collectors.toList());
    }

    private void isFileUploadInValid(MultipartFile multipartFile, BindingResult bindingResult) {
        String fileName = multipartFile.getOriginalFilename();
        if (fileName != null) {
            String[] fileSplit = fileName.split("\\.");
            String extension = fileSplit[fileSplit.length - 1];
            if (!extension.equals("doc") && !extension.equals("docx") && !extension.equals("pdf")) {

                bindingResult.rejectValue("attachFile", "message.0022");
            }
            if (multipartFile.isEmpty()) {
                bindingResult.rejectValue("attachFile", "message.0023");
            }
            if (multipartFile.getSize() >= 5242880) {
                bindingResult.rejectValue("attachFile", "message.0024");
            }
        }
    }

    private boolean isDateOfBirthInvalid(LocalDate dob) {
        if (dob != null) {
            TemporalAmount amount = Period.of(18, 0, 0);
            LocalDate dobValid = LocalDate.now().minus(amount);
            return dobValid.isBefore(dob);
        }
        return false;
    }

    private Set<CandidateSkill> convertSkillIdToCandidateSkill(Candidate candidate, Long[] skillIdArr) {
        return Arrays.stream(skillIdArr).map(skillId -> new CandidateSkill(candidate, Skill.builder().id(skillId).build())).collect(Collectors.toSet());
    }
}



