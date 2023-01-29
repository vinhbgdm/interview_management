package com.fa.ims.enums;

public enum CandidatePosition {
    BACKEND_DEVELOPER("Backend Developer"),
    BUSINESS_ANALYST("Business Analyst"),
    TESTER("Tester"),
    HR("HR"),
    PROJECT_MANAGER("Project manager");

    private final String displayValue;

    CandidatePosition(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
