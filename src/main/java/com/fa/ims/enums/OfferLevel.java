package com.fa.ims.enums;

public enum OfferLevel {
    FRESHER_1("Fresher 1"),
    JUNIOR_2_1("Junior 2.1"),
    JUNIOR_2_2("Junior 2.2"),
    SENIOR_3_1("Senior 3.1"),
    SENIOR_3_2("Senior 3.2"),
    DELIVER("Delivery"),
    LEADER("Leader"),
    MANAGER("Manager"),
    VICE_HEAD("Vice Head");

    private final String displayValue;

    OfferLevel(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }

}
