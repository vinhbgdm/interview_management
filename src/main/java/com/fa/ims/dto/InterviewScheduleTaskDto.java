package com.fa.ims.dto;
import com.fa.ims.entity.Candidate;
import com.sendgrid.helpers.mail.objects.Attachments;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class InterviewScheduleTaskDto {
    private Candidate candidate;
    private String recruiterOwnerName;
    private LocalDateTime scheduleTimeFrom;
    private LocalDateTime scheduleTimeTo;
    private String location;
    private String meetingId;
    private Attachments attachments;
}