package com.fa.ims.entity;

import com.fa.ims.dto.LastUsernameDto;
import com.fa.ims.dto.OfferReminderDto;
import com.fa.ims.enums.Department;
import com.fa.ims.enums.Gender;
import com.fa.ims.dto.UserListDto;
import com.fa.ims.enums.UserRole;
import com.fa.ims.enums.UserStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.BeanUtils;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "users")
@NamedNativeQuery(
        name = "findLastUsername",
        query = "SELECT  u.username   FROM dev_ims_db.users as u\n" +
                "where u.username like ?1\n" +
                "ORDER BY u.username DESC limit 1",
        resultSetMapping = "lastUsername"
)
@SqlResultSetMapping(
        name = "lastUsername",
        classes = {
                @ConstructorResult(
                        targetClass = LastUsernameDto.class,
                        columns = {
                                @ColumnResult(name = "username", type = String.class)
                        })})
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String username;

    @NotNull
    private String fullName;

    @NotNull
    private String email;

    private LocalDate dob;

    @NotNull
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull
    private String phone;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Department department;

    @OneToMany(mappedBy = "recruiter", cascade = CascadeType.ALL)
    private Set<Candidate> candidateSet;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    private String note;

    private String address;



    @OneToMany(mappedBy = "approvedBy")
    private Set<Offer> offerApproved;
    @OneToMany(mappedBy = "recruiterOwner")
    private Set<Offer> offerRecruiterOwnerList;
    @OneToMany(mappedBy = "recruiterOwner", cascade = CascadeType.ALL)
    private Set<InterviewSchedule> interviewSchedule;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<InterviewScheduleUser> interviewScheduleUserSet;

    public UserListDto convertToDisplayDto() {
        UserListDto userListDto = new UserListDto();
        BeanUtils.copyProperties(this, userListDto);
        return userListDto;
    }
    @OneToMany(mappedBy = "user")
    private Set<PasswordResetToken> passwordResetToken;
}
