package com.fa.ims.enums;

public enum Gender {
    MALE ("Male"),
    FEMALE ("Female"),
    OTHERS ("Others");

    private final String displayValue;

    Gender(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
