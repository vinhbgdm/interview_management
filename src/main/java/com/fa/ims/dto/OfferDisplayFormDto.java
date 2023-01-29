package com.fa.ims.dto;

import com.fa.ims.entity.Candidate;
import com.fa.ims.entity.User;
import com.fa.ims.enums.CandidatePosition;
import com.fa.ims.enums.Department;
import com.fa.ims.enums.OfferStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OfferDisplayFormDto {
    private Long id;

    private Candidate candidate;

    private User approvedBy;

    private Department department;

    private OfferStatus offerStatus;

    private CandidatePosition position;

    private String note;

}
