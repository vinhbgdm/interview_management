package com.fa.ims.enums;

public enum CandidateHighestLevel {
    HIGH_SCHOOL ("High school"),
    BACHELOR_DEGREE ("Bachelor's Degree"),
    MASTER_DEGREE ("Master Degree"),
    PHD ("PhD");

    private final String displayValue;

    private CandidateHighestLevel(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
