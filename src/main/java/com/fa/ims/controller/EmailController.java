package com.fa.ims.controller;

import com.fa.ims.dto.EmailDto;
import com.fa.ims.dto.InterviewScheduleDisplayDto;
import com.fa.ims.entity.InterviewSchedule;
import com.fa.ims.entity.InterviewScheduleUser;
import com.fa.ims.entity.User;
import com.fa.ims.enums.InterviewScheduleStatus;
import com.fa.ims.service.EmailService;
import com.fa.ims.service.InterviewScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@AllArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final InterviewScheduleService interviewScheduleService;
    private final SpringTemplateEngine springTemplateEngine;

    @GetMapping("api/interviews/email/{id}")
    public void sendEmail(@PathVariable Long id){

        InterviewSchedule interviewSchedule = interviewScheduleService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found Interview Schedule with id" + id));

        List<InterviewScheduleStatus> checkInterviewStatus = Stream.of(InterviewScheduleStatus.OPEN, InterviewScheduleStatus.INVITED)
                .filter( s -> s.equals(interviewSchedule.getInterviewStatus()))
                .collect(Collectors.toList());

        if (checkInterviewStatus.isEmpty()){
            throw new IllegalArgumentException("Invalid Interview status " + interviewSchedule.getInterviewStatus());
        }

        InterviewScheduleDisplayDto interviewScheduleDisplayDto = new InterviewScheduleDisplayDto();
        BeanUtils.copyProperties(interviewSchedule, interviewScheduleDisplayDto);

        interviewScheduleDisplayDto.setInterviewerSet(interviewSchedule.getInterviewerSet()
                .stream()
                .map(InterviewScheduleUser::getUser)
                .collect(Collectors.toList()));

        Context context = new Context();
        context.setVariable("interviewScheduleDisplayDto", interviewScheduleDisplayDto);
        String contentTemplate = springTemplateEngine.process("email-template/interview-email",context);

        EmailDto emailSend = new EmailDto();

        //Set email Candidate
        emailSend.setEmailToList(Collections.singletonList(interviewSchedule.getCandidate().getEmail()));
//        emailSend.setEmailToList(Collections.singletonList("ims.software.fjb@gmail.com"));

        //Set email Interviewer
        List<String> listEmailCc = (interviewScheduleDisplayDto.getInterviewerSet()
                                                            .stream()
                                                            .map(User::getEmail)
                                                            .collect(Collectors.toList()));

        //Set email Recruiter add list CC
        listEmailCc.add(interviewScheduleDisplayDto.getRecruiterOwner().getEmail());

        //Set all email CC of (Interview + Recruiter)
        emailSend.setEmailCcList(listEmailCc);

        //Set Subject:
        emailSend.setSubject("Invitation to interview with IMS Software for the [" +
                interviewScheduleDisplayDto.getCandidate().getCandidatePosition().getDisplayValue() + "] position");
        emailSend.setBody(contentTemplate);

        try {
            emailService.sendEmail(emailSend);
            if (interviewSchedule.getInterviewStatus().equals(InterviewScheduleStatus.OPEN)){
                interviewSchedule.setInterviewStatus(InterviewScheduleStatus.INVITED);
                interviewScheduleService.update(interviewSchedule);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
