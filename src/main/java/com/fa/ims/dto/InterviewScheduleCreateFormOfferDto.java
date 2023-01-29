package com.fa.ims.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InterviewScheduleCreateFormOfferDto {
    private Long id;
    private String title;
    private String note;
    private Long candidateId;
    Set<OfferInterviewerDto> listInterviewer;
}
