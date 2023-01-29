package com.fa.ims.shedulingtasks;

//import com.fa.ims.constant.CronConstants;

import com.fa.ims.constant.CommonConstants;
import com.fa.ims.constant.CronConstants;
import com.fa.ims.dto.EmailDto;
import com.fa.ims.dto.OfferReminderDto;
import com.fa.ims.service.EmailService;
import com.fa.ims.service.OfferService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Component
public class OfferScheduleReminder {
    private final OfferService offerService;
    private final SpringTemplateEngine springTemplateEngine;
    private final EmailService emailService;

    @Value("${app.portal-urL}")
    String contextPath;
    public OfferScheduleReminder(OfferService offerService, SpringTemplateEngine springTemplateEngine, EmailService emailService) {
        this.offerService = offerService;
        this.springTemplateEngine = springTemplateEngine;
        this.emailService = emailService;
    }

    @Scheduled(cron = CronConstants.EVERY_8AM)
    public void sendOfferReminder() {
        List<OfferReminderDto> offerReminderDtoList = offerService.getOfferReminder();

        offerReminderDtoList.forEach(offerReminderDto -> {
            Context context = new Context();
            context.setVariable("offerReminderDto", offerReminderDto);
            context.setVariable("contextPath", contextPath);
            String contentTemplate = springTemplateEngine.process("/email-template/EM-02", context);
            String subject = "[IMS][Reminder][" + LocalDate.now().format(DateTimeFormatter.ofPattern(CommonConstants.FORMAT_DATE)) + "] Approve Offer before due date is reached";

            EmailDto emailSend = EmailDto.builder().emailToList(Collections.singletonList(offerReminderDto.getEmail()))
                    .subject(subject)
                    .body(contentTemplate)
                    .build();

            try {
                emailService.sendEmail(emailSend);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}