package com.fa.ims.dto;

import com.fa.ims.constant.CommonConstants;
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
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;


@Getter
@Setter
@ToString
public class UserParamDto {

    private Long id;

    @NotBlank(message = "{message.004}")
    private String fullName;

    @NotBlank(message = "{message.004}")
    @Email(message = "{message.0011}")
    private String email;

    @DateTimeFormat(pattern = CommonConstants.FORMAT_DATE)
    private LocalDate dob;



    @Pattern(regexp = "^[0-9]\\d*$", message = "{message.0019}")
    private String phone;

    @NotNull(message = "{message.003}")
    private UserRole userRole;

    @NotNull(message = "{message.003}")
    private Department department;

    @Size(max = 5000)
    private String address;

    @NotNull(message = "{message.003}")
    private Gender gender;

    private Set<Candidate> candidateSet;

    @NotNull(message = "{message.003}")
    private UserStatus userStatus;

    @Size(max = 5000)
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
