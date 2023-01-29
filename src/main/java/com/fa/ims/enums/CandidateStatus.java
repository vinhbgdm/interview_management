package com.fa.ims.enums;

public enum CandidateStatus {

    OPEN("Open"),
    BANNED("Banned"),
    WAITING_FOR_INTERVIEW("Waiting for interview"),
    IN_PROGRESS("In progress"),
    CANCELLED_INTERVIEW("Cancelled interview"),
    PASSED_INTERVIEW("Passed interview"),
    FAILED_INTERVIEW("Failed interview"),
    WAITING_FOR_APPROVAL("Waiting for approval"),
    APPROVED_OFFER("Approved offer"),
    REJECTED_OFFER("Rejected offer"),
    WAITING_FOR_RESPONSE("Waiting for response"),
    ACCEPTED_OFFER("Accepted offer"),
    DECLINED_OFFER("Declined offer"),
    CANCELLED_OFFER("Cancelled offer");

    private final String displayValue;

    CandidateStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
