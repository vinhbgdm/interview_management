package com.fa.ims.dto;

import com.fa.ims.entity.Candidate;
import com.fa.ims.entity.InterviewSchedule;
import com.fa.ims.entity.InterviewScheduleUser;
import com.fa.ims.entity.Offer;
import com.fa.ims.enums.Department;
import com.fa.ims.enums.Gender;
import com.fa.ims.enums.UserRole;
import com.fa.ims.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@ToString
public class UserDisplayDto {

    private Long id;

    private String username;

    private String fullName;

    private String email;
    private LocalDate dob;

    private String password;
    private String phone;

    private UserRole userRole;

    private Department department;
    private String address;

    private Gender gender;

    private Set<Candidate> candidateSet;

    private UserStatus userStatus;
    private String note;

    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    private Set<Offer> offerApproved;

    private Set<Offer> offerRecruiterOwnerList;

    private Set<InterviewSchedule> interviewSchedule;

    private Set<InterviewScheduleUser> interviewScheduleUserSet;
}
