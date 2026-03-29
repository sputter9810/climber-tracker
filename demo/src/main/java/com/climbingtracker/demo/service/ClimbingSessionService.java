package com.climbingtracker.demo.service;

import com.climbingtracker.demo.model.entity.ClimbingSession;
import com.climbingtracker.demo.repository.ClimbingSessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClimbingSessionService {

    private final ClimbingSessionRepository climbingSessionRepository;

    public ClimbingSessionService(ClimbingSessionRepository climbingSessionRepository) {
        this.climbingSessionRepository = climbingSessionRepository;
    }

    public List<ClimbingSession> getAllSessions() {
        return climbingSessionRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getSessionDate().compareTo(a.getSessionDate()))
                .toList();
    }

    public ClimbingSession saveSession(ClimbingSession climbingSession) {
        return climbingSessionRepository.save(climbingSession);
    }

    public ClimbingSession getSessionById(Long id) {
        return climbingSessionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Session not found with id: " + id));
    }

    public void deleteSession(Long id) {
        if (!climbingSessionRepository.existsById(id)) {
            throw new IllegalArgumentException("Session not found with id: " + id);
        }
        climbingSessionRepository.deleteById(id);
    }
}