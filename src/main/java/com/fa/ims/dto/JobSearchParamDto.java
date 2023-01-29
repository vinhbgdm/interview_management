package com.fa.ims.dto;

import lombok.Setter;

@Setter
public class JobSearchParamDto extends SearchParamDto {
    private String status;

    public String getStatus() {
        return status;
    }
}
