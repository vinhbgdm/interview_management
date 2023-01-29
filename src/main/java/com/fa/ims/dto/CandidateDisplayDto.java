package com.fa.ims.dto;

import com.fa.ims.entity.CandidateSkill;
import com.fa.ims.entity.User;
import com.fa.ims.enums.CandidateHighestLevel;
import com.fa.ims.enums.CandidatePosition;
import com.fa.ims.enums.CandidateStatus;
import com.fa.ims.enums.Gender;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@ToString
public class CandidateDisplayDto {
    private Long id;
    private String fullName;
    private String email;
    private Gender gender;
    private LocalDate dob;
    private String address;
    private String note;
    private String phone;
    private String attachFile;
    private CandidatePosition candidatePosition;
    private Set<CandidateSkill> candidateSkillSet;
    private Integer yearExperience;
    private CandidateHighestLevel highestLevel;
    private CandidateStatus candidateStatus;
    private User recruiter;

    private String createdBy;

    private LocalDateTime createdDate;

    private String lastModifiedBy;

    private LocalDateTime lastModifiedDate;
}
