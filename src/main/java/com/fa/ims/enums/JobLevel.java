package com.fa.ims.enums;

public enum JobLevel {

    FRESHER ("Fresher"),
    JUNIOR ("Junior"),
    SENIOR ("Senior"),
    LEADER ("Leader"),
    TRAINER ("Trainer"),
    MENTOR ("Mentor");

    private final String displayValue;

    JobLevel(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
