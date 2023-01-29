package com.fa.ims.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReminderInterviewEmailDto {
    private String scheduleTitle;
    private LocalDateTime scheduleTimeFrom;
    private LocalDateTime scheduleTimeTo;
    private Long candidateId;
    private String candidateName;
    private String candidatePosition;
    private Long recruiterId;
    private String meetingId;
    private String email;
}
