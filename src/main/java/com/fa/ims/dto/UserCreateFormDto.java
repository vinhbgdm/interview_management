package com.fa.ims.dto;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fa.ims.constant.CommonConstants;
import com.fa.ims.enums.Department;
import com.fa.ims.enums.Gender;
import com.fa.ims.enums.UserRole;
import com.fa.ims.enums.UserStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserCreateFormDto {
	private Long id;
	
	@NotBlank(message = "{message.004}")
    private String fullName;

    @NotBlank(message = "{message.004}")
    @Email(message = "{message.0011}")
    private String email;

    @DateTimeFormat(pattern = CommonConstants.FORMAT_DATE)
    private LocalDate dob;

    @Size(max = 5000)
    private String address;
    
    @Pattern(regexp = "^[0-9]\\d*$", message = "{message.0019}")
    private String phone;
    
    @NotNull(message = "{message.003}")
    private Gender gender;
    
    @NotNull(message = "{message.003}")
    private UserRole userRole;
    
    @NotNull(message = "{message.003}")
    private Department department;
    
    @NotNull(message = "{message.003}")
    private UserStatus userStatus;
    
    private String note;
}
