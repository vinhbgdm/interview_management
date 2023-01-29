package com.fa.ims.enums;

public enum InterviewScheduleStatus {
    OPEN ("Open"),
    INVITED ("Invited"),
    INTERVIEWED ("Interviewed"),
    CANCELLED ("Cancelled");

    private final String displayValue;

    InterviewScheduleStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
