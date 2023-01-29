package com.fa.ims.controller.rest;

import com.fa.ims.dto.InterviewScheduleCreateFormOfferDto;
import com.fa.ims.dto.InterviewerUserCreateFormOfferDto;
import com.fa.ims.dto.OfferInterviewerDto;
import com.fa.ims.entity.InterviewSchedule;
import com.fa.ims.entity.User;
import com.fa.ims.service.InterviewScheduleService;
import com.fa.ims.service.InterviewScheduleUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/interview")
public class InterviewScheduleResource {

    private final InterviewScheduleUserService interviewScheduleUserService;
    private final InterviewScheduleService interviewScheduleService;

    public InterviewScheduleResource(InterviewScheduleUserService interviewScheduleUserService,
                                     InterviewScheduleService interviewScheduleService) {
        this.interviewScheduleUserService = interviewScheduleUserService;
        this.interviewScheduleService = interviewScheduleService;
    }

    @GetMapping("/{id}/get-user")
    public ResponseEntity<?> getUserByInterviewScheduleId(@PathVariable Long id) {
        List<User> userList = interviewScheduleUserService.findUserByInterviewScheduleId(id);
        if (!userList.isEmpty()) {
            List<InterviewerUserCreateFormOfferDto> interviewerUserCreateFormOfferDtoList
                    = userList.stream().map(user ->
                    new InterviewerUserCreateFormOfferDto(user.getId(), user.getUsername())
            ).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(
                    interviewerUserCreateFormOfferDtoList);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInterviewScheduleById(@PathVariable Long id) {
        Optional<InterviewSchedule> interviewScheduleOptional = interviewScheduleService.findById(id);
        if (interviewScheduleOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        InterviewSchedule interviewSchedule = interviewScheduleOptional.get();

        Set<OfferInterviewerDto> offerInterviewerDtoSet = interviewSchedule.getInterviewerSet()
                .stream().map(setRecruiter -> new OfferInterviewerDto(setRecruiter.getId(),
                        setRecruiter.getUser().getUsername()))
                .collect(Collectors.toSet());

        InterviewScheduleCreateFormOfferDto interviewScheduleCreateFormOfferDto =
                new InterviewScheduleCreateFormOfferDto(interviewSchedule.getId(),
                        interviewSchedule.getTitle(),
                        interviewSchedule.getNote(),
                        interviewSchedule.getCandidate().getId(), offerInterviewerDtoSet);

        return ResponseEntity.status(HttpStatus.OK).body(interviewScheduleCreateFormOfferDto);
    }
}
