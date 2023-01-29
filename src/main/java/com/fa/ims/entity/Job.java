package com.fa.ims.entity;

import com.fa.ims.dto.ViewListJobManagerRecruiterDto;
import com.fa.ims.enums.JobLevel;
import com.fa.ims.enums.JobStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public class Job extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_title", length = 500)
    private String jobTitle;

    @OneToMany(mappedBy = "job",cascade = CascadeType.ALL)
    private Set<JobSkill> requiredSkillSet;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "salary_from")
    private BigDecimal salaryFrom;

    @Column(name = "salary_to")
    private BigDecimal salaryTo;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private Set<JobBenefit> jobBenefitSet;

    @Column(name = "working_address", length = 2000)
    private String workingAddress;

    @Column(name = "job_level")
    private String jobLevel;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_status")
    private JobStatus jobStatus;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private Set<InterviewSchedule> interviewSchedule;

    public static ViewListJobManagerRecruiterDto convertToViewListJobManagerRecruiterDto(Job job) {
        ViewListJobManagerRecruiterDto viewListJobManagerRecruiterDto = new ViewListJobManagerRecruiterDto();
        BeanUtils.copyProperties(job, viewListJobManagerRecruiterDto);

        viewListJobManagerRecruiterDto.setRequiredSkills(
                job.getRequiredSkillSet()
                        .stream()
                        .map(jobSkill -> !jobSkill.isDeleted() ? jobSkill.getSkill().getSkillName() : null)
                        .filter(Objects::nonNull)
                        .collect(Collectors.joining(", "))
        );
        viewListJobManagerRecruiterDto.setJobStatus(
                job.getJobStatus().getDisplayValue()
        );
        return viewListJobManagerRecruiterDto;
    }
}
