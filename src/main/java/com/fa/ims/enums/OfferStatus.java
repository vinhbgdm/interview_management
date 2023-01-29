package com.fa.ims.enums;

public enum OfferStatus {
    WAITING_FOR_APPROVAL("Waiting for approval"),
    APPROVED_OFFER("Approved offer"),
    REJECTED_OFFER("Rejected offer"),
    CANCELLED_OFFER("Cancelled offer");

    private final String displayValue;

    private OfferStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
