package com.fa.ims.shedulingtasks;
import com.fa.ims.constant.CronConstants;
import com.fa.ims.dto.EmailDto;
import com.fa.ims.dto.InterviewScheduleTaskDto;
import com.fa.ims.entity.InterviewSchedule;
import com.fa.ims.service.EmailService;
import com.fa.ims.service.InterviewScheduleService;
import com.sendgrid.helpers.mail.objects.Attachments;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
@Component
@Service
public class InterviewSchedulingTask {
    private final EmailService emailService;
    private final InterviewScheduleService interviewScheduleService;
    private final SpringTemplateEngine springTemplateEngine;

    @Value("${app.file.location.cv}")
    String fileLocation;

    @Value("${app.portal-urL}")
    private String portalUrl;

    public InterviewSchedulingTask(EmailService emailService, InterviewScheduleService interviewScheduleService, SpringTemplateEngine springTemplateEngine) {
        this.emailService = emailService;
        this.interviewScheduleService = interviewScheduleService;
        this.springTemplateEngine = springTemplateEngine;
    }

    @Scheduled(cron = CronConstants.EVERY_8AM)
    public void remindEmail() {
        //retrieve InterviewSchedule having scheduleTimeFrom are today
        Specification<InterviewSchedule> queryCondition = (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("scheduleTimeFrom"),
                        LocalDate.now().atTime(0, 0),
                        LocalDate.now().atTime(23,59));
        List<InterviewSchedule> interviewScheduleList = interviewScheduleService.findAll(queryCondition);
        //InterviewSchedule -> user -> email
        interviewScheduleList.forEach(this::sendEmailInterview);
    }

    public void sendEmailInterview( InterviewSchedule interviewSchedule ) {
        //candidate + user
        List<String> emailList = new ArrayList<>();
        interviewSchedule.getInterviewerSet()
                .forEach(interviewScheduleUser -> emailList.add(interviewScheduleUser.getUser().getEmail()));
        InterviewScheduleTaskDto interviewScheduleTaskDto = new InterviewScheduleTaskDto();
        BeanUtils.copyProperties(interviewSchedule, interviewScheduleTaskDto);
        interviewScheduleTaskDto.setRecruiterOwnerName(
                interviewSchedule.getRecruiterOwner().getUsername()
        );
        Context context = new Context();
        context.setVariable("interviewScheduleTaskDto", interviewScheduleTaskDto);
        context.setVariable("contextPath", portalUrl);
        String contentTemplate = springTemplateEngine.process("email-template/reminder-email", context);
        EmailDto emailDto = new EmailDto();
        emailDto.setEmailToList(emailList);
        emailDto.setSubject("no-reply-email-IMS-system [" + interviewSchedule.getTitle() + "] ");
        emailDto.setBody(contentTemplate);
        if (interviewSchedule.getCandidate().getAttachFile() != null){
            String fileName =  interviewScheduleTaskDto.getCandidate().getAttachFile();
            interviewScheduleTaskDto.getCandidate().setAttachFile(fileName.replace("\\", "/"));
            try (InputStream inputStream = Files.newInputStream(Paths.get(fileLocation + "\\" + interviewScheduleTaskDto.getCandidate().getAttachFile()))) {
                Attachments attachments = new Attachments
                        .Builder(interviewScheduleTaskDto.getCandidate().getAttachFile().split("/")[1], inputStream)
                        .withType("application/pdf")
                        .build();
                emailDto.setAttachments(attachments);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            emailService.sendEmail(emailDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}