package com.fa.ims.dto;

import com.fa.ims.entity.Candidate;
import com.fa.ims.entity.InterviewSchedule;
import com.fa.ims.entity.User;
import com.fa.ims.enums.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class OfferDisplayDto {
    private Long id;
    private Candidate candidate;
    private InterviewSchedule interviewSchedule;
    private ContractType contractType;
    private CandidatePosition position;
    private OfferStatus offerStatus;
    private OfferLevel offerLevel;
    private Department department;
    private User approvedBy;
    private User recruiterOwner;
    private LocalDate contractFrom;
    private LocalDate contractTo;
    private LocalDate dueDate;
    private BigDecimal basicSalary;
    private String note;
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;
}
