package com.fa.ims.enums;

public enum InterviewResult {

    FAIL ("Fail"),
    PASS ("Pass");

    private final String displayValue;

    InterviewResult(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
