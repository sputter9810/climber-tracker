package com.climbingtracker.demo.model.enums;

public enum ClimbingType {
    BOULDER("Boulder"),
    TOP_ROPE("Top Rope");

    private final String displayName;

    ClimbingType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}