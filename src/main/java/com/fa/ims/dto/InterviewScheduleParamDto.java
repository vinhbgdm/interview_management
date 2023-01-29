package com.fa.ims.dto;
import com.fa.ims.constant.CommonConstants;
import com.fa.ims.entity.Job;
import com.fa.ims.enums.CandidatePosition;
import com.fa.ims.enums.InterviewResult;
import com.fa.ims.enums.InterviewScheduleStatus;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class InterviewScheduleParamDto {
    private Long id;

    @NotBlank(message = "{message.003}")
    private String title;

    @NotNull(message = "{message.003}")
    private Long candidateId;

    @NotEmpty(message = "{message.003}")
    private Long[] interviewerId;

    @DateTimeFormat(pattern = CommonConstants.FORMAT_DATE_TIME)
    @NotNull(message = "{message.003}")
    private LocalDateTime scheduleTimeFrom;

    @DateTimeFormat(pattern = CommonConstants.FORMAT_DATE_TIME)
    @NotNull(message = "{message.003}")
    private LocalDateTime scheduleTimeTo;

    private InterviewResult interviewResult;

    private InterviewScheduleStatus interviewStatus;

    private String note;

    @NotBlank(message = "{message.003}")
    private String location;

    @NotNull(message = "{message.003}")
    private Job job;

    @NotNull(message = "{message.003}")
    private Long recruiterOwnerId;

    private String meetingId;

    private Long offerId;

    @NotNull(message = "{message.003}")
    private CandidatePosition position;

    @DateTimeFormat(pattern = CommonConstants.FORMAT_DATE)
    @NotNull(message = "{message.003}")
    private LocalDate displayDate;
}
