package com.climbingtracker.demo.model.entity;

import com.climbingtracker.demo.model.enums.Gym;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "climbing_sessions")
public class ClimbingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate sessionDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gym gym;

    @Column(length = 1000)
    private String notes;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClimbEntry> climbEntries = new ArrayList<>();

    public ClimbingSession() {
    }

    public Long getId() {
        return id;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public Gym getGym() {
        return gym;
    }

    public void setGym(Gym gym) {
        this.gym = gym;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<ClimbEntry> getClimbEntries() {
        return climbEntries;
    }

    public void setClimbEntries(List<ClimbEntry> climbEntries) {
        this.climbEntries = climbEntries;
    }

    public void addClimbEntry(ClimbEntry climbEntry) {
        climbEntries.add(climbEntry);
        climbEntry.setSession(this);
    }

    public void removeClimbEntry(ClimbEntry climbEntry) {
        climbEntries.remove(climbEntry);
        climbEntry.setSession(null);
    }
}