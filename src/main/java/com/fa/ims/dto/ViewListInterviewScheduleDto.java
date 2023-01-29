package com.fa.ims.dto;

import com.fa.ims.entity.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ViewListInterviewScheduleDto extends BaseEntity {
    private Long id;

    private String title;

    private String candidateName;

    private String jobTitle;

    private LocalDateTime scheduleTimeFrom;

    private LocalDateTime scheduleTimeTo;

    private String interviewResult;

    private String interviewStatus;

}
