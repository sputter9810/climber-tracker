package com.climbingtracker.demo.controller;

import com.climbingtracker.demo.model.entity.ClimbEntry;
import com.climbingtracker.demo.model.entity.ClimbingSession;
import com.climbingtracker.demo.model.enums.ClimbingType;
import com.climbingtracker.demo.model.enums.Gym;
import com.climbingtracker.demo.service.ClimbingSessionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/sessions")
public class ClimbingSessionController {

    private final ClimbingSessionService climbingSessionService;

    public ClimbingSessionController(ClimbingSessionService climbingSessionService) {
        this.climbingSessionService = climbingSessionService;
    }

    @GetMapping
    public String listSessions(Model model) {
        model.addAttribute("sessions", climbingSessionService.getAllSessions());
        return "sessions/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("climbingSession", new ClimbingSession());
        model.addAttribute("gyms", Gym.values());
        return "sessions/new";
    }

    @PostMapping
    public String createSession(@ModelAttribute ClimbingSession climbingSession) {
        climbingSessionService.saveSession(climbingSession);
        return "redirect:/sessions";
    }

    @GetMapping("/{id}")
    public String viewSession(@PathVariable Long id, Model model) {
        ClimbingSession climbingSession = climbingSessionService.getSessionById(id);

        model.addAttribute("climbingSession", climbingSession);
        model.addAttribute("climbEntry", new ClimbEntry());
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

        return "sessions/detail";
    }

    @PostMapping("/{id}/delete")
    public String deleteSession(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {
        try {
            climbingSessionService.deleteSession(id);
            redirectAttributes.addFlashAttribute("successMessage", "Session deleted successfully.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/sessions";
    }

    private ClimbingType[] getAllowedClimbingTypes(Gym gym) {
        if (gym == Gym.PULSE_WARNERS_BAY) {
            return new ClimbingType[]{ClimbingType.BOULDER};
        }
        return new ClimbingType[]{ClimbingType.BOULDER, ClimbingType.TOP_ROPE};
    }
}