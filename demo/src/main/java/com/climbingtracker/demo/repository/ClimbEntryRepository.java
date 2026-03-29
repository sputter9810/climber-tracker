package com.climbingtracker.demo.repository;

import com.climbingtracker.demo.model.entity.ClimbEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClimbEntryRepository extends JpaRepository<ClimbEntry, Long> {
}