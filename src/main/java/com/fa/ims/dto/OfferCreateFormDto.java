package com.fa.ims.dto;

import com.fa.ims.constant.CommonConstants;
import com.fa.ims.enums.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class OfferCreateFormDto {
    @NotNull(message = "{message.003}")
    private Long candidateId;

    private Long interviewScheduleId;

    @NotNull(message = "{message.003}")
    private ContractType contractType;

    @NotNull(message = "{message.003}")
    private CandidatePosition position;

    private OfferStatus offerStatus;

    @NotNull(message = "{message.003}")
    private OfferLevel offerLevel;


//    private Long[] interviewerList;

    @NotNull(message = "{message.003}")
    private Department department;

    @NotNull(message = "{message.003}")
    private Long approvedBy;

    @NotNull(message = "{message.003}")
    private Long recruiterId;


    @NotNull(message = "{message.003}")
    @FutureOrPresent(message = "{message.0031}")
    @DateTimeFormat(pattern = CommonConstants.FORMAT_DATE)
    private LocalDate contractFrom;

    @NotNull(message = "{message.003}")
    @DateTimeFormat(pattern = CommonConstants.FORMAT_DATE)
    private LocalDate contractTo;

    @NotNull(message = "{message.003}")
    @DateTimeFormat(pattern = CommonConstants.FORMAT_DATE)
    @FutureOrPresent(message = "{message.0030}")
    private LocalDate dueDate;

    @NotNull(message = "{message.003}")
    @DecimalMin(value = "0.0", inclusive = false, message = "must be float number")
    private BigDecimal basicSalary;

    @Size(max = 5000)
    private String note;

}
