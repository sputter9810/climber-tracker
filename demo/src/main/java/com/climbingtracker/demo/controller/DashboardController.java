package com.climbingtracker.demo.controller;

import com.climbingtracker.demo.dto.GradeAnalyticsRow;
import com.climbingtracker.demo.dto.ProgressAnalyticsRow;
import com.climbingtracker.demo.model.enums.ClimbingType;
import com.climbingtracker.demo.model.enums.Gym;
import com.climbingtracker.demo.service.AnalyticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    private final AnalyticsService analyticsService;

    public DashboardController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalSessions", analyticsService.getTotalSessions());
        model.addAttribute("totalClimbs", analyticsService.getTotalClimbs());
        model.addAttribute("averageClimbsPerSession", String.format("%.2f", analyticsService.getAverageClimbsPerSession()));
        model.addAttribute("completionRate", String.format("%.2f", analyticsService.getCompletionRate()));
        model.addAttribute("flashRate", String.format("%.2f", analyticsService.getFlashRate()));
        model.addAttribute("averageAttemptedGrade", String.format("%.2f", analyticsService.getAverageAttemptedGrade()));

        model.addAttribute("hardestCompletedGrade", analyticsService.getHardestCompletedGrade());
        model.addAttribute("hardestFlashedGrade", analyticsService.getHardestFlashedGrade());
        model.addAttribute("averageCompletedGrade", String.format("%.2f", analyticsService.getAverageCompletedGrade()));
        model.addAttribute("averageAttemptsPerClimb", String.format("%.2f", analyticsService.getAverageAttemptsPerClimb()));

        model.addAttribute("warnersBayClimbs", analyticsService.getClimbsAtGym(Gym.PULSE_WARNERS_BAY));
        model.addAttribute("adamstownClimbs", analyticsService.getClimbsAtGym(Gym.PULSE_ADAMSTOWN));

        model.addAttribute("boulderClimbs", analyticsService.getClimbsByType(ClimbingType.BOULDER));
        model.addAttribute("topRopeClimbs", analyticsService.getClimbsByType(ClimbingType.TOP_ROPE));

        model.addAttribute("boulderCompletionRate", String.format("%.2f", analyticsService.getCompletionRateByType(ClimbingType.BOULDER)));
        model.addAttribute("topRopeCompletionRate", String.format("%.2f", analyticsService.getCompletionRateByType(ClimbingType.TOP_ROPE)));

        List<GradeAnalyticsRow> gradeBreakdown = analyticsService.getGradeBreakdown();
        List<ProgressAnalyticsRow> progressOverTime = analyticsService.getProgressOverTime();

        model.addAttribute("gradeBreakdown", gradeBreakdown);
        model.addAttribute("progressOverTime", progressOverTime);

        model.addAttribute("gradeChartLabels",
                gradeBreakdown.stream()
                        .map(GradeAnalyticsRow::getGrade)
                        .toList());

        model.addAttribute("gradeChartAttempts",
                gradeBreakdown.stream()
                        .map(GradeAnalyticsRow::getAttemptsCount)
                        .toList());

        List<ProgressAnalyticsRow> progressAscending = progressOverTime.stream()
                .sorted((a, b) -> a.getSessionDate().compareTo(b.getSessionDate()))
                .toList();

        model.addAttribute("progressChartLabels",
                progressAscending.stream()
                        .map(row -> row.getSessionDate().toString())
                        .toList());

        model.addAttribute("progressAvgGrades",
                progressAscending.stream()
                        .map(ProgressAnalyticsRow::getAverageAttemptedGrade)
                        .toList());

        model.addAttribute("progressHardestGrades",
                progressAscending.stream()
                        .map(ProgressAnalyticsRow::getHardestCompletedGrade)
                        .toList());

        return "dashboard";
    }
}