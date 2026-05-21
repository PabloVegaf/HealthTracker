package com.healthtracker.backend.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "daily_records")
/**
 * Entidad principal del seguimiento diario de salud y entrenamiento.
 */
public class DailyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "weight_kg", precision = 5, scale = 2)
    private BigDecimal weightKg;

    @Column(name = "body_fat_pct", precision = 4, scale = 2)
    private BigDecimal bodyFatPct;

    @Column(name = "kcal_consumed")
    private Integer kcalConsumed;

    @Column(name = "kcal_expended")
    private Integer kcalExpended;

    @OneToMany(mappedBy = "dailyRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseSession> sessions = new ArrayList<>();

    @Column(name = "exercise_duration_min")
    private Integer exerciseDurationMin;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public DailyRecord() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public BigDecimal getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(BigDecimal weightKg) {
        this.weightKg = weightKg;
    }

    public BigDecimal getBodyFatPct() {
        return bodyFatPct;
    }

    public void setBodyFatPct(BigDecimal bodyFatPct) {
        this.bodyFatPct = bodyFatPct;
    }

    public Integer getKcalConsumed() {
        return kcalConsumed;
    }

    public void setKcalConsumed(Integer kcalConsumed) {
        this.kcalConsumed = kcalConsumed;
    }

    public Integer getKcalExpended() {
        return kcalExpended;
    }

    public void setKcalExpended(Integer kcalExpended) {
        this.kcalExpended = kcalExpended;
    }

    public List<ExerciseSession> getSessions() {
        return sessions;
    }

    public void addSession(ExerciseSession session) {
        sessions.add(session);
        session.setDailyRecord(this);
        recalculateDuration();
    }

    public void removeSession(ExerciseSession session) {
        sessions.remove(session);
        session.setDailyRecord(null);
        recalculateDuration();
    }

    private void recalculateDuration() {
        this.exerciseDurationMin = sessions.stream()
                .mapToInt(ExerciseSession::getDurationMin)
                .sum();
    }

    public Integer getExerciseDurationMin() {
        return exerciseDurationMin;
    }

    public void setExerciseDurationMin(Integer exerciseDurationMin) {
        this.exerciseDurationMin = exerciseDurationMin;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return (
            "DailyRecord{" +
            "id=" +
            id +
            ", userId=" +
            (user != null ? user.getId() : null) +
            ", recordDate=" +
            recordDate +
            ", weightKg=" +
            weightKg +
            ", bodyFatPct=" +
            bodyFatPct +
            ", kcalConsumed=" +
            kcalConsumed +
            ", kcalExpended=" +
            kcalExpended +
            ", sessions=" +
            sessions.stream()
                .map(s -> s.getExerciseCategory().getName() + "(" + s.getDurationMin() + "min)")
                .collect(Collectors.toList()) +
            ", exerciseDurationMin=" +
            exerciseDurationMin +
            ", notes='" +
            notes +
            '\'' +
            ", createdAt=" +
            createdAt +
            ", updatedAt=" +
            updatedAt +
            '}'
        );
    }
}
