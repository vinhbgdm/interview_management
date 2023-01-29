package com.fa.ims.service.impl;

import com.fa.ims.dto.EmailDto;
import com.fa.ims.dto.EmailSendUserDto;
import com.fa.ims.service.EmailService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.Objects;

@Service
public class SendGridMailService implements EmailService {

    private final String SENDER_NAME = "IMS Software";
    private final String SENDER_EMAIL = "minhtu93932@gmail.com";
    private final String API_KEY = "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";

    @Override
    public boolean sendEmail(EmailDto emailDto) throws IOException {
        Objects.requireNonNull(emailDto.getEmailToList());

        // Setting sender information
        Email sender = new Email(SENDER_EMAIL, SENDER_NAME);

        // Setting email to, email cc, email bcc
        Personalization personalization = new Personalization();
        emailDto.getEmailToList().forEach(e -> personalization.addTo(new Email(e)));

        if (Objects.nonNull(emailDto.getEmailCcList())) {
            emailDto.getEmailCcList().forEach(e -> personalization.addCc(new Email(e)));
        }

        if (Objects.nonNull(emailDto.getEmailBccList())) {
            emailDto.getEmailBccList().forEach(e -> personalization.addBcc(new Email(e)));
        }

        // Setting content email
        Content content = new Content("text/html", emailDto.getBody());

        Mail mail = new Mail();
        mail.setFrom(sender);
        mail.setSubject(emailDto.getSubject());
        mail.addPersonalization(personalization);
        mail.addContent(content);

        if (emailDto.getAttachments() != null){
            mail.addAttachments(emailDto.getAttachments());
        }

        try {
            SendGrid sendGrid = new SendGrid(API_KEY);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            return response.getStatusCode() == 202;
        } catch (IOException exception) {
            return false;
        }
    }

}