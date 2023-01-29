package com.fa.ims.enums;

public enum UserRole {
    ROLE_RECRUITER ("Recruiter"),
    ROLE_INTERVIEWER ("Interviewer"),
    ROLE_MANAGER ("Manager"),
    ROLE_ADMIN ("Admin");

    private final String displayValue;

    UserRole(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
