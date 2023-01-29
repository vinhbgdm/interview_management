package com.fa.ims.dto;
import com.fa.ims.entity.Candidate;
import com.fa.ims.entity.Job;
import com.fa.ims.entity.User;
import com.fa.ims.enums.CandidatePosition;
import com.fa.ims.enums.InterviewResult;
import com.fa.ims.enums.InterviewScheduleStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class InterviewScheduleDisplayDto {
    private Long id;
    private String title;
    private Candidate candidate;
    private List<User> interviewerSet;
    private LocalDateTime scheduleTimeFrom;
    private LocalDateTime scheduleTimeTo;
    private InterviewResult interviewResult;
    private InterviewScheduleStatus interviewStatus;
    private String note;
    private String location;
    private String jobTitle;
    private User recruiterOwner;
    private String meetingId;
    private String candidatePosition;
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;
}