package com.healthtracker.backend.controller;

import com.healthtracker.backend.dto.BodyMeasurementRequest;
import com.healthtracker.backend.dto.DailyRecordRequest;
import com.healthtracker.backend.dto.DailyRecordResponse;
import com.healthtracker.backend.dto.DashboardSummary;
import com.healthtracker.backend.dto.UserConfigRequest;
import com.healthtracker.backend.dto.UserConfigResponse;
import com.healthtracker.backend.model.BodyMeasurement;
import com.healthtracker.backend.model.ExerciseCategory;
import com.healthtracker.backend.service.HealthTrackerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class HealthTrackerController {

    private final HealthTrackerService service;

    public HealthTrackerController(HealthTrackerService service) {
        this.service = service;
    }

    // ---------- Daily Records ----------

    @GetMapping("/daily-records")
    public ResponseEntity<List<DailyRecordResponse>> getRecords(
            @RequestParam(required = false) LocalDate start,
            @RequestParam(required = false) LocalDate end) {
        Long userId = 1L; // TODO: get from JWT
        List<DailyRecordResponse> records;
        if (start != null && end != null) {
            records = service.getRecordsBetween(userId, start, end);
        } else {
            records = service.getRecords(userId);
        }
        return ResponseEntity.ok(records);
    }

    @PostMapping("/daily-records")
    public ResponseEntity<DailyRecordResponse> createRecord(@RequestBody DailyRecordRequest request) {
        Long userId = 1L; // TODO: get from JWT
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createRecord(userId, request));
    }

    @PutMapping("/daily-records/{id}")
    public ResponseEntity<DailyRecordResponse> updateRecord(@PathVariable Long id,
                                                             @RequestBody DailyRecordRequest request) {
        Long userId = 1L; // TODO: get from JWT
        return ResponseEntity.ok(service.updateRecord(id, userId, request));
    }

    @DeleteMapping("/daily-records/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        Long userId = 1L; // TODO: get from JWT
        service.deleteRecord(id, userId);
        return ResponseEntity.noContent().build();
    }

    // ---------- Body Measurements ----------

    @GetMapping("/body-measurements")
    public ResponseEntity<List<BodyMeasurement>> getMeasurements() {
        Long userId = 1L; // TODO: get from JWT
        return ResponseEntity.ok(service.getMeasurements(userId));
    }

    @PostMapping("/body-measurements")
    public ResponseEntity<BodyMeasurement> createMeasurement(@RequestBody BodyMeasurementRequest request) {
        Long userId = 1L; // TODO: get from JWT
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createMeasurement(userId, request));
    }

    @DeleteMapping("/body-measurements/{id}")
    public ResponseEntity<Void> deleteMeasurement(@PathVariable Long id) {
        Long userId = 1L; // TODO: get from JWT
        service.deleteMeasurement(id, userId);
        return ResponseEntity.noContent().build();
    }

    // ---------- Exercise Categories ----------

    @GetMapping("/exercise-categories")
    public ResponseEntity<List<ExerciseCategory>> getCategories() {
        Long userId = 1L; // TODO: get from JWT
        return ResponseEntity.ok(service.getCategories(userId));
    }

    @PostMapping("/exercise-categories")
    public ResponseEntity<ExerciseCategory> createCategory(@RequestParam String name) {
        Long userId = 1L; // TODO: get from JWT
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createCategory(userId, name));
    }

    @DeleteMapping("/exercise-categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        Long userId = 1L; // TODO: get from JWT
        service.deleteCategory(id, userId);
        return ResponseEntity.noContent().build();
    }

    // ---------- User Config ----------

    @GetMapping("/user-config")
    public ResponseEntity<UserConfigResponse> getUserConfig() {
        Long userId = 1L; // TODO: get from JWT
        UserConfigResponse config = service.getUserConfig(userId);
        return config != null ? ResponseEntity.ok(config) : ResponseEntity.notFound().build();
    }

    @PutMapping("/user-config")
    public ResponseEntity<UserConfigResponse> updateUserConfig(@RequestBody UserConfigRequest request) {
        Long userId = 1L; // TODO: get from JWT
        return ResponseEntity.ok(service.updateUserConfig(userId, request));
    }

    // ---------- Dashboard ----------

    @GetMapping("/dashboard/summary")
    public ResponseEntity<DashboardSummary> getDashboard() {
        Long userId = 1L; // TODO: get from JWT
        return ResponseEntity.ok(service.getDashboardSummary(userId));
    }
}
