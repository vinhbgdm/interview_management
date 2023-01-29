package com.fa.ims.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForgotPasswordDto {
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$",message="Invalid password form")
    private String password;

    @NotBlank
    private String confirmPassword;

    private String validatedToken;
}
