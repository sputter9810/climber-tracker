package com.climbingtracker.demo.model.enums;

public enum Gym {
    PULSE_WARNERS_BAY("Pulse Warners Bay"),
    PULSE_ADAMSTOWN("Pulse Adamstown");

    private final String displayName;

    Gym(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}