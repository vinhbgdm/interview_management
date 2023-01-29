package com.fa.ims.dto;

import com.sendgrid.helpers.mail.objects.Attachments;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class EmailDto {
    private List<String> emailToList;
    private List<String> emailCcList;
    private List<String> emailBccList;
    private String subject;
    private String body;
    private Attachments attachments;
}
