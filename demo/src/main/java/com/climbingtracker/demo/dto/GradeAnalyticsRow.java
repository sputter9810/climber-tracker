package com.climbingtracker.demo.dto;

public class GradeAnalyticsRow {

    private final int grade;
    private final long attemptsCount;
    private final long completedCount;
    private final long flashedCount;
    private final double completionRate;
    private final double flashRate;
    private final double averageAttempts;

    public GradeAnalyticsRow(int grade,
                             long attemptsCount,
                             long completedCount,
                             long flashedCount,
                             double completionRate,
                             double flashRate,
                             double averageAttempts) {
        this.grade = grade;
        this.attemptsCount = attemptsCount;
        this.completedCount = completedCount;
        this.flashedCount = flashedCount;
        this.completionRate = completionRate;
        this.flashRate = flashRate;
        this.averageAttempts = averageAttempts;
    }

    public int getGrade() {
        return grade;
    }

    public long getAttemptsCount() {
        return attemptsCount;
    }

    public long getCompletedCount() {
        return completedCount;
    }

    public long getFlashedCount() {
        return flashedCount;
    }

    public double getCompletionRate() {
        return completionRate;
    }

    public double getFlashRate() {
        return flashRate;
    }

    public double getAverageAttempts() {
        return averageAttempts;
    }
}