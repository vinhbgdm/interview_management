package com.fa.ims.dto;

import lombok.Setter;

@Setter
public class CandidateSearchParamDto extends SearchParamDto{
    private String status;
    public String getStatus() {
        return status;
    }
}
