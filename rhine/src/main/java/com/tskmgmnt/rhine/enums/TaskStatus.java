package com.tskmgmnt.rhine.enums;

public enum TaskStatus {
    PENDING("PENDING"),
    ONGOING("ONGOING"),
    COMPLETED("COMPLETED"),
    CANCELLED("CANCELLED");

    private final String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
