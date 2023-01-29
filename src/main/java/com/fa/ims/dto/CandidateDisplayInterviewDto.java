package com.fa.ims.dto;

import com.fa.ims.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CandidateDisplayInterviewDto {
    private Long id;
    private String fullName;
    private User recruiter;
}
