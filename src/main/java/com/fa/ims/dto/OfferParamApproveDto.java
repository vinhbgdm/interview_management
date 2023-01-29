package com.fa.ims.dto;

import com.fa.ims.entity.Candidate;
import com.fa.ims.entity.InterviewSchedule;
import com.fa.ims.entity.User;
import com.fa.ims.enums.*;
import lombok.*;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OfferParamApproveDto {


    private OfferStatus offerStatus;

    @Size(max = 100)
    private String note;
}
