package com.fa.ims.dto;

import com.fa.ims.enums.JobStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class JobDisplayDto {
    private Long id;
    private String jobTitle;
    private List<String> requiredSkillSet;
    private String startDate;
    private String endDate;
    private String salaryFrom;
    private String salaryTo;
    private List<String> jobBenefitSet;
    private String workingAddress;
    private String jobLevel;
    private String description;
    private JobStatus jobStatus;
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;
}