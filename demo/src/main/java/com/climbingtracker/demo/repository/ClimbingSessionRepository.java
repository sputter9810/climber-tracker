package com.climbingtracker.demo.repository;

import com.climbingtracker.demo.model.entity.ClimbingSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClimbingSessionRepository extends JpaRepository<ClimbingSession, Long> {
}