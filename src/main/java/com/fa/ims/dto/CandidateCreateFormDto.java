package com.fa.ims.dto;

import com.fa.ims.constant.CommonConstants;
import com.fa.ims.enums.CandidateHighestLevel;
import com.fa.ims.enums.CandidatePosition;
import com.fa.ims.enums.CandidateStatus;
import com.fa.ims.enums.Gender;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class CandidateCreateFormDto {

    @NotBlank(message = "{message.004}")
    private String fullName;

    @NotBlank(message = "{message.004}")
    @Email(message = "{message.0011}")
    private String email;

    @NotNull(message = "{message.003}")
    private Gender gender;

    @DateTimeFormat(pattern = CommonConstants.FORMAT_DATE)
    private LocalDate dob;

    @Size(max = 5000)
    private String address;

    @Size(max = 5000)
    private String note;

    //    @NotBlank(message = "{message.004}")
    @Pattern(regexp = "^[0-9]\\d*$", message = "{message.0019}")
    private String phone;

    private MultipartFile attachFile;

    @NotNull(message = "{message.003}")
    private CandidatePosition candidatePosition;

    @NotEmpty(message = "{message.003}")
    private Long[] candidateSkillList;

    @Min(value = 0, message = "{message.0018}")
    private Integer yearExperience;

    @NotNull(message = "{message.003}")
    private CandidateHighestLevel highestLevel;

    @NotNull(message = "{message.003}")
    private CandidateStatus candidateStatus;

    @NotNull(message = "{message.003}")
    private Long recruiterId;
}
