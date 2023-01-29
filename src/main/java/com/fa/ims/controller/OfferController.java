package com.fa.ims.controller;

import com.fa.ims.constant.CommonConstants;
import com.fa.ims.dto.OfferDisplayFormDto;
import com.fa.ims.entity.*;
import com.fa.ims.enums.CandidateStatus;
import com.fa.ims.enums.Department;
import com.fa.ims.enums.UserRole;
import com.fa.ims.security.SecurityUtil;
import com.fa.ims.service.*;
import com.fa.ims.util.EnumUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.fa.ims.dto.*;
import com.fa.ims.entity.Candidate;
import com.fa.ims.entity.Offer;
import com.fa.ims.enums.OfferStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/offers")
public class OfferController extends BaseController {
    private final UserService userService;
    private final CandidateService candidateService;
    private final InterviewScheduleUserService interviewScheduleUserService;
    private final OfferService offerService;
    private final InterviewScheduleService interviewScheduleService;

    @Value("${app.file.location.cv}")
    String fileLocation;

    public OfferController(UserService userService, CandidateService candidateService,
                           InterviewScheduleUserService interviewScheduleUserService, OfferService offerService,
                           InterviewScheduleService interviewScheduleService) {
        this.userService = userService;
        this.candidateService = candidateService;
        this.interviewScheduleUserService = interviewScheduleUserService;
        this.offerService = offerService;
        this.interviewScheduleService = interviewScheduleService;
    }

    @GetMapping("/{offerId}")
    public String showViewOffer(@PathVariable(name = "offerId") Long id, Model model) {

        Offer offer = offerService.findById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find Offer with id" + id));
        if (SecurityUtil.getCurrentUserLogin().get().equals(offer.getApprovedBy().getUsername())) {
            model.addAttribute("showButtonApprove", true);

        }
        if (offer.getInterviewSchedule() != null) {
            List<User> interviewerList = interviewScheduleUserService.findUserByInterviewScheduleId(offer.getInterviewSchedule().getId());
            model.addAttribute("interviewerList", interviewerList);
        }

        OfferDisplayDto offerDisplayDto = new OfferDisplayDto();
        BeanUtils.copyProperties(offer, offerDisplayDto);
        model.addAttribute("offerDisplayDto", offerDisplayDto);
        return "offer/view-offer";
    }

    @GetMapping
    public String showListOffer(@RequestParam(name = "action", required = false) String action,
                                OfferSearchParamDto searchParamDto,
                                Model model) {
        Specification<Offer> basicConditions = offerService.undeletedSpec();
        String currentUsernameLogin = SecurityUtil.getCurrentUserLogin().orElseThrow(EntityNotFoundException::new);
        Long currentUserLoginId = userService.findIdByUsername(currentUsernameLogin);

        //hiển thị thông tin offer cần approve (check cả due date)
        if (SecurityUtil.getListUserLoginRole().get(0) == UserRole.ROLE_MANAGER
                || SecurityUtil.getListUserLoginRole().get(0) == UserRole.ROLE_ADMIN) {
            OfferReminderDto offerReminderDto = offerService.getTotalOfferNeedApproveById(currentUserLoginId);
            if (offerReminderDto != null) {
                model.addAttribute("approveDto", offerReminderDto);
            }
        }

        //Lọc danh sách để approve, check ngày due date
        if (action != null && action.equals("approve")) {
            basicConditions = basicConditions.and(offerService.filterApproveView(currentUserLoginId));
        }

        //Recruiter chỉ xem được những offer mà mình Owner
        if (SecurityUtil.getListUserLoginRole().get(0) == UserRole.ROLE_RECRUITER) {
            basicConditions = basicConditions.and(offerService.filterRecruiterView(userService.findIdByUsername(currentUsernameLogin)));
        }

        if (StringUtils.hasLength(searchParamDto.getSearchKey())) {
            basicConditions = basicConditions.and(offerService.buildSpecForSearch(searchParamDto.getSearchKey()));
        }

        if (StringUtils.hasLength(searchParamDto.getDepartment())
                && EnumUtils.isValidName(Department.class, searchParamDto.getDepartment())) {
            Specification<Offer> departmentCondition = (root, query, criteriaBuilder)
                    -> criteriaBuilder.equal(root.get("department"), Department.valueOf(searchParamDto.getDepartment().toUpperCase()));
            basicConditions = basicConditions.and(departmentCondition);
        }

        if (StringUtils.hasLength(searchParamDto.getStatus())
                && EnumUtils.isValidName(OfferStatus.class, searchParamDto.getStatus())) {
            Specification<Offer> statusCondition = (root, query, criteriaBuilder)
                    -> criteriaBuilder.equal(root.get("offerStatus"), OfferStatus.valueOf(searchParamDto.getStatus().toUpperCase()));
            basicConditions = basicConditions.and(statusCondition);
        }

        if (SecurityUtil.getListUserLoginRole().get(0) == UserRole.ROLE_RECRUITER){
            System.out.println(SecurityUtil.getCurrentUserLogin().get());
            Specification<Offer> recruiterOwnerCondition = (root, query, criteriaBuilder)
                    -> criteriaBuilder.equal(root.get("recruiterOwner"), userService.findByUsername(SecurityUtil.getCurrentUserLogin().get()).get());
            basicConditions = basicConditions.and(recruiterOwnerCondition);
        }

        //setup page with sort and search
        List<Sort.Order> orderList = buildSortByKey(searchParamDto.getSort());
        Page<Offer> offerPage = offerService.findAll(basicConditions,
                PageRequest.of(searchParamDto.getPage(), searchParamDto.getSize(), Sort.by(orderList))
        );
        model.addAttribute("searchKey", searchParamDto.getSearchKey());
        model.addAttribute("department", searchParamDto.getDepartment());
        model.addAttribute("status", searchParamDto.getStatus());
        if (offerPage.isEmpty()) {
            model.addAttribute("contentPage", Page.empty());
            return "offer/list-offer";
        }
        List<OfferDisplayFormDto> offerDisplayFormDtoList = offerPage.getContent().stream()
                .map(Offer::convertFormToDisplayDto).collect(Collectors.toList());

        Page<OfferDisplayFormDto> contentPage = new PageImpl<>(offerDisplayFormDtoList,
                PageRequest.of(searchParamDto.getPage(), searchParamDto.getSize(), Sort.by(orderList)),
                offerPage.getTotalElements());
        model.addAttribute("contentPage", contentPage);
        model.addAttribute("sort", searchParamDto.getSort());
        return "offer/list-offer";
    }

    @GetMapping("/create")
    public String showAddOffer(Model model) {

        model.addAttribute("candidateList", getCandidateCreateFormOffer());
        //List of recruiter (recruiter owner)
        model.addAttribute("recruiterList", getRecruiterListDisplayDto());
        //List of manager (approvedBy)
        model.addAttribute("managerList", getManagerListDisplayDto());
        model.addAttribute("offerCreateFormDto", new OfferCreateFormDto());
        return "offer/create-offer";
    }

    @PostMapping("/create")
    public String addOffer(@ModelAttribute("offerCreateFormDto") @Valid OfferCreateFormDto offerCreateFormDto,
                           BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        if (offerService.existsCandidateInAnotherOffer(offerCreateFormDto.getCandidateId())) {
            bindingResult.rejectValue("candidateId", "message.0038");
        }

        if (offerCreateFormDto.getContractFrom() != null && offerCreateFormDto.getContractTo() != null
                && offerCreateFormDto.getContractFrom().isAfter(offerCreateFormDto.getContractTo())) {
            bindingResult.rejectValue("contractTo", "message.0026",
                    "To date must be lager than from date");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("candidateList", getCandidateCreateFormOffer());
            model.addAttribute("recruiterList", getRecruiterListDisplayDto());
            model.addAttribute("managerList", getManagerListDisplayDto());
            model.addAttribute("interviewList",
                    interviewScheduleService.findByCandidateId(offerCreateFormDto.getCandidateId()));
            return "offer/create-offer";
        }

        Offer offer = new Offer();
        BeanUtils.copyProperties(offerCreateFormDto, offer, "candidateId", "interviewScheduleId",
                "approvedBy", "recruiterId");

        Candidate candidate = candidateService.findById(offerCreateFormDto.getCandidateId()).orElseThrow(()
                -> new EntityNotFoundException("Cannot find candidate"));
        offer.setCandidate(candidate);

        if (offerCreateFormDto.getInterviewScheduleId() != null) {
            offer.setInterviewSchedule(InterviewSchedule.builder().id(offerCreateFormDto.getInterviewScheduleId()).build());
        }
        if (offerCreateFormDto.getApprovedBy() != null) {
            offer.setApprovedBy(User.builder().id(offerCreateFormDto.getApprovedBy()).build());
        }
        if (offerCreateFormDto.getRecruiterId() != null) {
            offer.setRecruiterOwner(User.builder().id(offerCreateFormDto.getRecruiterId()).build());
        }

        offer.setOfferStatus(OfferStatus.WAITING_FOR_APPROVAL);
        try {
            offerService.createNew(offer);
            redirectAttributes.addFlashAttribute("messageSuccess", "message.025");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("messageFail", "message.024");
        }
        return "redirect:/offers";
    }

    @GetMapping("/edit/{offerId}")
    public String showUpdateOffer(@PathVariable(name = "offerId") Long id, Model model){
        Offer offer = offerService.findById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find Offer with id" + id));

        //FIXME: check status already approved of candidate throw exception to reject editing
        if (SecurityUtil.getListUserLoginRole().get(0) == UserRole.ROLE_RECRUITER
                && offer.getOfferStatus() == OfferStatus.APPROVED_OFFER ) {
            return "redirect:/403";
        }

        OfferParamDto offerParamDto = new OfferParamDto();
        BeanUtils.copyProperties(offer, offerParamDto);

        offerParamDto.setApprovedBy(offer.getApprovedBy().getId());
        offerParamDto.setRecruiterId(offer.getRecruiterOwner().getId());

        if (offer.getInterviewSchedule() != null){
            offerParamDto.setInterviewScheduleId(offer.getInterviewSchedule().getId());
        }

        offerParamDto.setCandidateId(offer.getCandidate().getId());

        model.addAttribute("candidateList", getCandidateUpdateFormOffer(offer.getCandidate().getId()));
        //List of recruiter (recruiter owner)
        model.addAttribute("recruiterList", getRecruiterListDisplayDto());
        //List of manager (approvedBy)
        model.addAttribute("managerList", getManagerListDisplayDto());
        model.addAttribute("offerParamDto", offerParamDto);
        model.addAttribute("interviewList",
                interviewScheduleService.findByCandidateId(offer.getCandidate().getId()));
        return "offer/offer-update";
    }


    @Transactional
    @PostMapping("/edit/{offerId}")
    public String updateOffer(@PathVariable(name = "offerId") Long id,
                              @ModelAttribute("offerParamDto") @Valid OfferParamDto offerParamDto,
                           BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        Offer offer = offerService.findById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find Offer with id" + id));

        if (offerParamDto.getContractFrom() != null && offerParamDto.getContractTo() != null
                && offerParamDto.getContractFrom().isAfter(offerParamDto.getContractTo())) {
            bindingResult.rejectValue("contractTo", "message.0026",
                    "To date must be lager than from date");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("candidateList", getCandidateCreateFormOffer());
            model.addAttribute("recruiterList", getRecruiterListDisplayDto());
            model.addAttribute("managerList", getManagerListDisplayDto());
            model.addAttribute("interviewList",
                    interviewScheduleService.findByCandidateId(offerParamDto.getCandidateId()));
            return "offer/offer-update";
        }

        BeanUtils.copyProperties(offerParamDto, offer, "candidateId", "interviewScheduleId",
                "approvedBy", "recruiterId");

        Candidate candidate = candidateService.findById(offerParamDto.getCandidateId()).orElseThrow(()
                -> new EntityNotFoundException("Cannot find candidate"));

        offer.setCandidate(candidate);

        if (offerParamDto.getInterviewScheduleId() != null) {
            offer.setInterviewSchedule(InterviewSchedule.builder().id(offerParamDto.getInterviewScheduleId()).build());
        }
        if (offerParamDto.getApprovedBy() != null) {
            offer.setApprovedBy(User.builder().id(offerParamDto.getApprovedBy()).build());
        }
        if (offerParamDto.getRecruiterId() != null) {
            offer.setRecruiterOwner(User.builder().id(offerParamDto.getRecruiterId()).build());
        }

        try {
            offerService.update(offer);
            redirectAttributes.addFlashAttribute("messageSuccess", "message.030");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("messageFail", "message.029");
        }
        return "redirect:/offers";
    }

    @GetMapping("/{offerId}/approve")
    public String showApproveOffer(@PathVariable(name = "offerId") Long id, Model model) {

        Offer offer = offerService.findById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find Offer with id" + id));

        if (!SecurityUtil.getCurrentUserLogin().get().equals(offer.getApprovedBy().getUsername())) {
            return "redirect:/403";
        }

        OfferDisplayDto offerDisplayDto = new OfferDisplayDto();
        BeanUtils.copyProperties(offer, offerDisplayDto);
        model.addAttribute("offerDisplayDto", offerDisplayDto);
        return "offer/approve-offer";
    }

    @PostMapping("/{offerId}/approve")
    public String showApproveOffer(@PathVariable(name = "offerId") Long id,
                                   @ModelAttribute("offerParamApproveDto") @Valid OfferDisplayDto offerDisplayDto,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        Offer offer = offerService.findById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find Offer with id" + id));

        if (bindingResult.hasErrors()) {
//
//            OfferDisplayDto offerDisplayDto = new OfferDisplayDto();
//            BeanUtils.copyProperties(offer, offerDisplayDto);
//            model.addAttribute("offerDisplayDto", offerDisplayDto);
            return "offer/approve-offer";
        }
        offer.setOfferStatus(offerDisplayDto.getOfferStatus());
        offer.setNote(offerDisplayDto.getNote());
        offer.getCandidate().setCandidateStatus(CandidateStatus.valueOf(offerDisplayDto.getOfferStatus().name()));

        try {
            offerService.update(offer);
            redirectAttributes.addFlashAttribute("messageSuccess", "message.0037");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("messageFail", "message.0036");

        }
        return "redirect:/offers";
    }


    @GetMapping("/export/{fromDate}_{toDate}")
    public String exportToExcel
            (@PathVariable(name = "fromDate") @DateTimeFormat(pattern = CommonConstants.FORMAT_DATE) LocalDate fromDate,
             @PathVariable(name = "toDate") @DateTimeFormat(pattern = CommonConstants.FORMAT_DATE) LocalDate toDate,
             RedirectAttributes redirectAttributes, HttpServletResponse response) {
        response.setContentType("application/octet-stream");

        String fileName = fromDate.format(DateTimeFormatter.ofPattern(CommonConstants.FORMAT_DATE)) + "_"
                + toDate.format(DateTimeFormatter.ofPattern(CommonConstants.FORMAT_DATE));

        String headerValue = "attachment; filename=offers_" + fileName + ".xlsx";
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, headerValue);

        List<Offer> offerList = offerService.findAllByDateBetween(fromDate, toDate);
        if (offerList.isEmpty()) {
            redirectAttributes.addFlashAttribute("messageFail", "message.0027");
            return "redirect:/offers";
        }

        try {
            ExportExcelOfferService excelExporter = new ExportExcelOfferService(offerList);
            excelExporter.export(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/offers";
    }

    private List<CandidateCreateFormOfferDto> getCandidateCreateFormOffer() {
        return candidateService.findAllCandidateToCreateOffer().stream().map(candidate -> {
            CandidateCreateFormOfferDto candidateCreateFormOfferDto = new CandidateCreateFormOfferDto();
            BeanUtils.copyProperties(candidate, candidateCreateFormOfferDto);
            return candidateCreateFormOfferDto;
        }).collect(Collectors.toList());
    }

    private List<CandidateCreateFormOfferDto> getCandidateUpdateFormOffer(Long id) {
        return candidateService.findAllCandidateToUpdateOffer(id).stream().map(candidate -> {
            CandidateCreateFormOfferDto candidateCreateFormOfferDto = new CandidateCreateFormOfferDto();
            BeanUtils.copyProperties(candidate, candidateCreateFormOfferDto);
            return candidateCreateFormOfferDto;
        }).collect(Collectors.toList());
    }



    private List<RecruiterDisplayDto> getRecruiterListDisplayDto() {
        return userService.findAllRecruiter().stream().map(user -> {
            RecruiterDisplayDto recruiterDisplayDto = new RecruiterDisplayDto();
            BeanUtils.copyProperties(user, recruiterDisplayDto);
            return recruiterDisplayDto;
        }).collect(Collectors.toList());
    }

    private List<ManagerCreateOfferFormDto> getManagerListDisplayDto() {
        return userService.findAllManager().stream().map(user -> {
            ManagerCreateOfferFormDto managerCreateOfferFormDto = new ManagerCreateOfferFormDto();
            BeanUtils.copyProperties(user, managerCreateOfferFormDto);
            return managerCreateOfferFormDto;
        }).collect(Collectors.toList());
    }
}
