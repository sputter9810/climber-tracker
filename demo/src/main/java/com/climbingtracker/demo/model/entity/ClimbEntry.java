package com.climbingtracker.demo.model.entity;

import com.climbingtracker.demo.model.enums.ClimbingType;
import jakarta.persistence.*;

@Entity
@Table(name = "climb_entries")
public class ClimbEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String colour;

    @Column(nullable = false)
    private Integer grade;

    @Column(nullable = false)
    private String wallSection;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClimbingType climbingType;

    @Column(nullable = false)
    private Integer attempts;

    @Column(nullable = false)
    private Boolean completed;

    @Column(nullable = false)
    private Boolean flashed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private ClimbingSession session;

    public ClimbEntry() {
    }

    public Long getId() {
        return id;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getWallSection() {
        return wallSection;
    }

    public void setWallSection(String wallSection) {
        this.wallSection = wallSection;
    }

    public ClimbingType getClimbingType() {
        return climbingType;
    }

    public void setClimbingType(ClimbingType climbingType) {
        this.climbingType = climbingType;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Boolean getFlashed() {
        return flashed;
    }

    public void setFlashed(Boolean flashed) {
        this.flashed = flashed;
    }

    public ClimbingSession getSession() {
        return session;
    }

    public void setSession(ClimbingSession session) {
        this.session = session;
    }
}