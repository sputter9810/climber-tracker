package com.climbingtracker.demo.service;

import com.climbingtracker.demo.dto.GradeAnalyticsRow;
import com.climbingtracker.demo.dto.ProgressAnalyticsRow;
import com.climbingtracker.demo.model.entity.ClimbEntry;
import com.climbingtracker.demo.model.entity.ClimbingSession;
import com.climbingtracker.demo.model.enums.ClimbingType;
import com.climbingtracker.demo.model.enums.Gym;
import com.climbingtracker.demo.repository.ClimbEntryRepository;
import com.climbingtracker.demo.repository.ClimbingSessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private final ClimbingSessionRepository climbingSessionRepository;
    private final ClimbEntryRepository climbEntryRepository;

    public AnalyticsService(ClimbingSessionRepository climbingSessionRepository,
                            ClimbEntryRepository climbEntryRepository) {
        this.climbingSessionRepository = climbingSessionRepository;
        this.climbEntryRepository = climbEntryRepository;
    }

    public long getTotalSessions() {
        return climbingSessionRepository.count();
    }

    public long getTotalClimbs() {
        return climbEntryRepository.count();
    }

    public double getAverageClimbsPerSession() {
        long totalSessions = getTotalSessions();
        long totalClimbs = getTotalClimbs();

        if (totalSessions == 0) {
            return 0.0;
        }

        return (double) totalClimbs / totalSessions;
    }

    public double getCompletionRate() {
        List<ClimbEntry> climbs = climbEntryRepository.findAll();

        if (climbs.isEmpty()) {
            return 0.0;
        }

        long completedCount = climbs.stream()
                .filter(climb -> Boolean.TRUE.equals(climb.getCompleted()))
                .count();

        return ((double) completedCount / climbs.size()) * 100.0;
    }

    public double getFlashRate() {
        List<ClimbEntry> climbs = climbEntryRepository.findAll();

        if (climbs.isEmpty()) {
            return 0.0;
        }

        long flashedCount = climbs.stream()
                .filter(climb -> Boolean.TRUE.equals(climb.getFlashed()))
                .count();

        return ((double) flashedCount / climbs.size()) * 100.0;
    }

    public double getAverageAttemptedGrade() {
        List<ClimbEntry> climbs = climbEntryRepository.findAll();

        if (climbs.isEmpty()) {
            return 0.0;
        }

        return climbs.stream()
                .mapToInt(ClimbEntry::getGrade)
                .average()
                .orElse(0.0);
    }

    public int getHardestCompletedGrade() {
        return climbEntryRepository.findAll().stream()
                .filter(climb -> Boolean.TRUE.equals(climb.getCompleted()))
                .mapToInt(ClimbEntry::getGrade)
                .max()
                .orElse(0);
    }

    public int getHardestFlashedGrade() {
        return climbEntryRepository.findAll().stream()
                .filter(climb -> Boolean.TRUE.equals(climb.getFlashed()))
                .mapToInt(ClimbEntry::getGrade)
                .max()
                .orElse(0);
    }

    public double getAverageCompletedGrade() {
        return climbEntryRepository.findAll().stream()
                .filter(climb -> Boolean.TRUE.equals(climb.getCompleted()))
                .mapToInt(ClimbEntry::getGrade)
                .average()
                .orElse(0.0);
    }

    public double getAverageAttemptsPerClimb() {
        return climbEntryRepository.findAll().stream()
                .mapToInt(ClimbEntry::getAttempts)
                .average()
                .orElse(0.0);
    }

    public long getClimbsAtGym(Gym gym) {
        return climbEntryRepository.findAll().stream()
                .filter(climb -> climb.getSession() != null && climb.getSession().getGym() == gym)
                .count();
    }

    public long getClimbsByType(ClimbingType climbingType) {
        return climbEntryRepository.findAll().stream()
                .filter(climb -> climb.getClimbingType() == climbingType)
                .count();
    }

    public double getCompletionRateByType(ClimbingType climbingType) {
        List<ClimbEntry> climbsOfType = climbEntryRepository.findAll().stream()
                .filter(climb -> climb.getClimbingType() == climbingType)
                .toList();

        if (climbsOfType.isEmpty()) {
            return 0.0;
        }

        long completedCount = climbsOfType.stream()
                .filter(climb -> Boolean.TRUE.equals(climb.getCompleted()))
                .count();

        return ((double) completedCount / climbsOfType.size()) * 100.0;
    }

    public List<GradeAnalyticsRow> getGradeBreakdown() {
        List<GradeAnalyticsRow> rows = new ArrayList<>();
        List<ClimbEntry> climbs = climbEntryRepository.findAll();

        for (int grade = 1; grade <= 10; grade++) {
            int currentGrade = grade;

            List<ClimbEntry> climbsAtGrade = climbs.stream()
                    .filter(climb -> climb.getGrade() != null && climb.getGrade() == currentGrade)
                    .toList();

            long attemptsCount = climbsAtGrade.size();

            long completedCount = climbsAtGrade.stream()
                    .filter(climb -> Boolean.TRUE.equals(climb.getCompleted()))
                    .count();

            long flashedCount = climbsAtGrade.stream()
                    .filter(climb -> Boolean.TRUE.equals(climb.getFlashed()))
                    .count();

            double completionRate = attemptsCount == 0
                    ? 0.0
                    : ((double) completedCount / attemptsCount) * 100.0;

            double flashRate = attemptsCount == 0
                    ? 0.0
                    : ((double) flashedCount / attemptsCount) * 100.0;

            double averageAttempts = climbsAtGrade.stream()
                    .mapToInt(ClimbEntry::getAttempts)
                    .average()
                    .orElse(0.0);

            rows.add(new GradeAnalyticsRow(
                    currentGrade,
                    attemptsCount,
                    completedCount,
                    flashedCount,
                    completionRate,
                    flashRate,
                    averageAttempts
            ));
        }

        return rows;
    }

    public List<ProgressAnalyticsRow> getProgressOverTime() {
        List<ClimbingSession> sessions = climbingSessionRepository.findAll();

        Map<LocalDate, List<ClimbingSession>> sessionsByDate = sessions.stream()
                .filter(session -> session.getSessionDate() != null)
                .collect(Collectors.groupingBy(ClimbingSession::getSessionDate));

        return sessionsByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    List<ClimbingSession> sessionsOnDate = entry.getValue();

                    long sessionCount = sessionsOnDate.size();

                    List<ClimbEntry> climbsOnDate = sessionsOnDate.stream()
                            .flatMap(session -> session.getClimbEntries().stream())
                            .toList();

                    long climbCount = climbsOnDate.size();

                    double averageAttemptedGrade = climbsOnDate.stream()
                            .mapToInt(ClimbEntry::getGrade)
                            .average()
                            .orElse(0.0);

                    int hardestCompletedGrade = climbsOnDate.stream()
                            .filter(climb -> Boolean.TRUE.equals(climb.getCompleted()))
                            .mapToInt(ClimbEntry::getGrade)
                            .max()
                            .orElse(0);

                    return new ProgressAnalyticsRow(
                            date,
                            sessionCount,
                            climbCount,
                            averageAttemptedGrade,
                            hardestCompletedGrade
                    );
                })
                .toList();
    }
}