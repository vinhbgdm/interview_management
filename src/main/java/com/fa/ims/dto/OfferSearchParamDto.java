package com.fa.ims.dto;

import lombok.Setter;

@Setter
public class OfferSearchParamDto extends SearchParamDto{
    private String status;

    private String department;

    public String getStatus() {
        return status;
    }

    public String getDepartment() {
        return department;
    }
}
