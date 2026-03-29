package com.climbingtracker.demo.controller;

import com.climbingtracker.demo.model.entity.ClimbEntry;
import com.climbingtracker.demo.model.entity.ClimbingSession;
import com.climbingtracker.demo.model.enums.ClimbingType;
import com.climbingtracker.demo.model.enums.Gym;
import com.climbingtracker.demo.service.ClimbEntryService;
import com.climbingtracker.demo.service.ClimbingSessionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/sessions/{sessionId}/climbs")
public class ClimbEntryController {

    private final ClimbEntryService climbEntryService;
    private final ClimbingSessionService climbingSessionService;

    public ClimbEntryController(ClimbEntryService climbEntryService,
                                ClimbingSessionService climbingSessionService) {
        this.climbEntryService = climbEntryService;
        this.climbingSessionService = climbingSessionService;
    }

    @PostMapping
    public String addClimb(@PathVariable Long sessionId,
                           @ModelAttribute ClimbEntry climbEntry,
                           RedirectAttributes redirectAttributes) {
        try {
            climbEntryService.saveClimb(sessionId, climbEntry);
            redirectAttributes.addFlashAttribute("successMessage", "Climb added successfully.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/sessions/" + sessionId;
    }

    @GetMapping("/{climbId}/edit")
    public String showEditForm(@PathVariable Long sessionId,
                               @PathVariable Long climbId,
                               Model model) {
        ClimbingSession climbingSession = climbingSessionService.getSessionById(sessionId);

        model.addAttribute("climbingSession", climbingSession);
        model.addAttribute("climbEntry", climbEntryService.getClimbById(climbId));
        model.addAttribute("climbingTypes", getAllowedClimbingTypes(climbingSession.getGym()));

        model.addAttribute("warnersBoulderSections", new String[]{
                "Slab & Cave",
                "North Wall",
                "South Wall",
                "Lakeside",
                "Front Island",
                "Rear Island"
        });

        model.addAttribute("adamstownBoulderSections", new String[]{
                "Mini Cave",
                "Back Corner",
                "BLT",
                "Slab",
                "Island"
        });

        return "climbs/edit";
    }

    @PostMapping("/{climbId}/edit")
    public String updateClimb(@PathVariable Long sessionId,
                              @PathVariable Long climbId,
                              @ModelAttribute ClimbEntry climbEntry,
                              RedirectAttributes redirectAttributes) {
        try {
            climbEntryService.updateClimb(climbId, climbEntry);
            redirectAttributes.addFlashAttribute("successMessage", "Climb updated successfully.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/sessions/" + sessionId + "/climbs/" + climbId + "/edit";
        }

        return "redirect:/sessions/" + sessionId;
    }

    @PostMapping("/{climbId}/delete")
    public String deleteClimb(@PathVariable Long sessionId,
                              @PathVariable Long climbId,
                              RedirectAttributes redirectAttributes) {
        try {
            climbEntryService.deleteClimb(climbId);
            redirectAttributes.addFlashAttribute("successMessage", "Climb deleted successfully.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/sessions/" + sessionId;
    }

    private ClimbingType[] getAllowedClimbingTypes(Gym gym) {
        if (gym == Gym.PULSE_WARNERS_BAY) {
            return new ClimbingType[]{ClimbingType.BOULDER};
        }
        return new ClimbingType[]{ClimbingType.BOULDER, ClimbingType.TOP_ROPE};
    }
}