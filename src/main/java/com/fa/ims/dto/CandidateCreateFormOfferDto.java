package com.fa.ims.dto;

import com.fa.ims.entity.InterviewSchedule;
import com.fa.ims.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class CandidateCreateFormOfferDto {
    private Long id;
    private String fullName;
    private User recruiter;
    private Set<InterviewSchedule> interviewScheduleSet;

}
