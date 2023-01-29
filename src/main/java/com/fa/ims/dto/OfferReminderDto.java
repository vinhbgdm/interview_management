package com.fa.ims.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OfferReminderDto {
    private Long total;
    private Long approvedId;
//    private LocalDate dueDate;
    private String username;
    private String email;
    private String gender;
    private String fullName;

}
