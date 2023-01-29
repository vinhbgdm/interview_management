package com.fa.ims.dto;

import com.fa.ims.constant.CommonConstants;
import com.fa.ims.entity.InterviewSchedule;
import com.fa.ims.enums.JobLevel;
import com.fa.ims.enums.JobStatus;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class JobEditParamDto {
    private Long id;

    @NotBlank(message = "{message.003}")
    @Size(max = 500)
    private String jobTitle;

    @NotEmpty(message = "{message.003}")
    private Long[] skillIdList;

    @NotNull(message = "{message.003}")
    @DateTimeFormat(pattern = CommonConstants.FORMAT_DATE)
    private LocalDate startDate;

    @NotNull(message = "{message.003}")
    @DateTimeFormat(pattern = CommonConstants.FORMAT_DATE)
    private LocalDate endDate;

    @DecimalMin(value = "0.0", message = "Invalid format!")
    private BigDecimal salaryFrom;

    @NotNull(message = "{message.003}")
    @DecimalMin(value = "0.0", message = "Invalid format!")
    private BigDecimal salaryTo;

    @NotEmpty(message = "{message.003}")
    private Long[] benefitIdList;

    @Size(max = 2000)
    private String workingAddress;

    @NotEmpty(message = "{message.003}")
    private JobLevel[] jobLevel;

    @Column(length = 2000)
    private String description;

    private JobStatus jobStatus ;

    private Set<InterviewSchedule> interviewSchedule;

    private LocalDate createdBy;

    private LocalDate createdDate;

    private LocalDate lastModifiedBy;

    private LocalDate lastModifiedDate;

}

