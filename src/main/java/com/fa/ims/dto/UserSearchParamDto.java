package com.fa.ims.dto;

import lombok.Setter;

@Setter
public class UserSearchParamDto extends SearchParamDto{
    private String role;
    public String getRole() {
        return role;
    }
}
