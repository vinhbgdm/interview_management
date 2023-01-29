package com.fa.ims.entity;

import com.fa.ims.dto.CandidateDisplayFormDto;
import com.fa.ims.enums.CandidateHighestLevel;
import com.fa.ims.enums.CandidatePosition;
import com.fa.ims.enums.CandidateStatus;
import com.fa.ims.enums.Gender;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Nationalized;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public class Candidate extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nationalized
    @Column(name = "full_name")
    private String fullName;

    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate dob;

    @Column(length = 5000)
    private String address;

    @Column(length = 5000)
    private String note;

    private String phone;

    @Column(name = "attach_file")
    private String attachFile;

    @Enumerated(EnumType.STRING)
    @Column(name = "candidate_position")
    private CandidatePosition candidatePosition;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL)
    private Set<CandidateSkill> candidateSkillSet;

    @Column(name = "year_experience")
    private Integer yearExperience;

    @Enumerated(EnumType.STRING)
    @Column(name = "highest_level")
    private CandidateHighestLevel highestLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "candidate_status")
    private CandidateStatus candidateStatus;

    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    private User recruiter;

    @OneToMany(mappedBy = "candidate",cascade = CascadeType.ALL)
    private Set<InterviewSchedule> interviewScheduleSet;

    @OneToMany(mappedBy = "candidate",cascade = CascadeType.ALL)

    private Set<Offer> offerSet;

    public CandidateDisplayFormDto convertToDisplayDto() {
        CandidateDisplayFormDto candidateDisplayFormDto = new CandidateDisplayFormDto();
        BeanUtils.copyProperties(this, candidateDisplayFormDto);
        candidateDisplayFormDto.setRecruiterUsername(this.getRecruiter().getUsername());
        return candidateDisplayFormDto;
    }
}
