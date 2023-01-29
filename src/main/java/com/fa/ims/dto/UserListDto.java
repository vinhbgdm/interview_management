package com.fa.ims.dto;

import com.fa.ims.enums.UserRole;
import com.fa.ims.enums.UserStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserListDto {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private UserRole userRole;
    private UserStatus userStatus;
}
