package com.fa.ims.dto;

import com.fa.ims.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecruiterDisplayDto {
    private Long id;

    private String username;

    private UserRole userRole;
    private String fullName;
}
