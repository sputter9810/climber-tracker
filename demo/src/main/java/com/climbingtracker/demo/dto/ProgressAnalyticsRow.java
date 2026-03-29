package com.climbingtracker.demo.dto;

import java.time.LocalDate;

public class ProgressAnalyticsRow {

    private final LocalDate sessionDate;
    private final long sessionCount;
    private final long climbCount;
    private final double averageAttemptedGrade;
    private final int hardestCompletedGrade;

    public ProgressAnalyticsRow(LocalDate sessionDate,
                                long sessionCount,
                                long climbCount,
                                double averageAttemptedGrade,
                                int hardestCompletedGrade) {
        this.sessionDate = sessionDate;
        this.sessionCount = sessionCount;
        this.climbCount = climbCount;
        this.averageAttemptedGrade = averageAttemptedGrade;
        this.hardestCompletedGrade = hardestCompletedGrade;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public long getSessionCount() {
        return sessionCount;
    }

    public long getClimbCount() {
        return climbCount;
    }

    public double getAverageAttemptedGrade() {
        return averageAttemptedGrade;
    }

    public int getHardestCompletedGrade() {
        return hardestCompletedGrade;
    }
}