package com.fa.ims.entity;

import com.fa.ims.enums.CandidatePosition;
import com.fa.ims.enums.InterviewResult;
import com.fa.ims.enums.InterviewScheduleStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class InterviewSchedule extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @OneToMany(mappedBy = "interviewerSet", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<InterviewScheduleUser> interviewerSet;

    @Column(name = "schedule_time_from")
    private LocalDateTime scheduleTimeFrom;

    @Column(name = "schedule_time_to")
    private LocalDateTime scheduleTimeTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "interview_result")
    private InterviewResult interviewResult;

    @Enumerated(EnumType.STRING)
    @Column(name = "interview_status")
    private InterviewScheduleStatus interviewStatus;

    @Column(length = 2000)
    private String note;

    private String location;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User recruiterOwner;

    @Column(name = "meeting_id")
    private String meetingId;

    @OneToOne(mappedBy = "interviewSchedule")
    private Offer offer;

    @Enumerated(EnumType.STRING)
    @Column(name = "interview_position")
    private CandidatePosition position;

}
