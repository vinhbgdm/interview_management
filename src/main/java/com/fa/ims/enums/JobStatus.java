package com.fa.ims.enums;

public enum JobStatus {

    DRAFT("Draft"),
    OPEN ("Open"),
    CLOSED ("Closed");

    private final String displayValue;

    JobStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
