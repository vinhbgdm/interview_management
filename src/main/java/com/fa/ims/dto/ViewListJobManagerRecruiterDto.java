package com.fa.ims.dto;
import com.fa.ims.entity.BaseEntity;
import com.fa.ims.enums.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ViewListJobManagerRecruiterDto extends BaseEntity {
    private Long id;
    private String jobTitle;
    private String requiredSkills;
    private LocalDate startDate;
    private LocalDate endDate;
    private String jobLevel;
    private String jobStatus;
}