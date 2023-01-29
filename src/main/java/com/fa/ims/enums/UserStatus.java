package com.fa.ims.enums;

public enum UserStatus {
    ACTIVE ("Active"), DEACTIVATED ("Deactivated");

    private final String displayValue;

    UserStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
