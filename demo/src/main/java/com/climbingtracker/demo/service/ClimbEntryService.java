package com.climbingtracker.demo.service;

import com.climbingtracker.demo.model.entity.ClimbEntry;
import com.climbingtracker.demo.model.entity.ClimbingSession;
import com.climbingtracker.demo.repository.ClimbEntryRepository;
import org.springframework.stereotype.Service;

@Service
public class ClimbEntryService {

    private final ClimbEntryRepository climbEntryRepository;
    private final ClimbingSessionService climbingSessionService;

    public ClimbEntryService(ClimbEntryRepository climbEntryRepository,
                             ClimbingSessionService climbingSessionService) {
        this.climbEntryRepository = climbEntryRepository;
        this.climbingSessionService = climbingSessionService;
    }

    public ClimbEntry saveClimb(Long sessionId, ClimbEntry climbEntry) {
        validateClimbEntry(climbEntry);

        ClimbingSession climbingSession = climbingSessionService.getSessionById(sessionId);
        climbEntry.setSession(climbingSession);

        return climbEntryRepository.save(climbEntry);
    }

    public ClimbEntry getClimbById(Long climbId) {
        return climbEntryRepository.findById(climbId)
                .orElseThrow(() -> new IllegalArgumentException("Climb not found with id: " + climbId));
    }

    public void deleteClimb(Long climbId) {
        if (!climbEntryRepository.existsById(climbId)) {
            throw new IllegalArgumentException("Climb not found with id: " + climbId);
        }
        climbEntryRepository.deleteById(climbId);
    }

    public ClimbEntry updateClimb(Long climbId, ClimbEntry updatedClimbEntry) {
        validateClimbEntry(updatedClimbEntry);

        ClimbEntry existingClimb = getClimbById(climbId);

        existingClimb.setColour(updatedClimbEntry.getColour());
        existingClimb.setGrade(updatedClimbEntry.getGrade());
        existingClimb.setWallSection(updatedClimbEntry.getWallSection());
        existingClimb.setClimbingType(updatedClimbEntry.getClimbingType());
        existingClimb.setAttempts(updatedClimbEntry.getAttempts());
        existingClimb.setCompleted(updatedClimbEntry.getCompleted());
        existingClimb.setFlashed(updatedClimbEntry.getFlashed());

        return climbEntryRepository.save(existingClimb);
    }

    private void validateClimbEntry(ClimbEntry climbEntry) {
        if (climbEntry.getColour() == null || climbEntry.getColour().isBlank()) {
            throw new IllegalArgumentException("Colour is required.");
        }

        if (climbEntry.getWallSection() == null || climbEntry.getWallSection().isBlank()) {
            throw new IllegalArgumentException("Wall section is required.");
        }

        if (climbEntry.getGrade() == null || climbEntry.getGrade() < 1 || climbEntry.getGrade() > 10) {
            throw new IllegalArgumentException("Grade must be between 1 and 10.");
        }

        if (climbEntry.getClimbingType() == null) {
            throw new IllegalArgumentException("Climbing type is required.");
        }

        if (climbEntry.getAttempts() == null || climbEntry.getAttempts() < 1) {
            throw new IllegalArgumentException("Attempts must be at least 1.");
        }

        if (Boolean.TRUE.equals(climbEntry.getFlashed())) {
            if (!Boolean.TRUE.equals(climbEntry.getCompleted())) {
                throw new IllegalArgumentException("A flashed climb must also be completed.");
            }

            if (climbEntry.getAttempts() != 1) {
                throw new IllegalArgumentException("A flashed climb must have exactly 1 attempt.");
            }
        }
    }
}