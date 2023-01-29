package com.fa.ims.dto;

import com.fa.ims.enums.CandidatePosition;
import com.fa.ims.enums.CandidateStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CandidateDisplayFormDto {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private CandidatePosition candidatePosition;
    private String recruiterUsername;
    private CandidateStatus candidateStatus;
}
