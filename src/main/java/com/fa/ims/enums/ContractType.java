package com.fa.ims.enums;

public enum ContractType {
    TRIAL_TWO_MONTH("Trial 2 months"),
    TRAINEE_THREE_MONTH("Trainee 3 months"),
    ONE_YEAR("One year"),
    THREE_YEAR("Three years"),
    UNLIMITED("Unlimited");

    private final String displayValue;

    ContractType(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
