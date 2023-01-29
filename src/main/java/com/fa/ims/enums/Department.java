package com.fa.ims.enums;

public enum Department {
    IT("IT"),
    HR("HR"),
    FINANCE ("Finance"),
    COMMUNICATION("Communication"),
    MARKETING("Marketing"),
    ACCOUNTING("Accounting");

    private final String displayValue;

    Department(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
