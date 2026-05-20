package com.healthtracker.backend.service;

import com.healthtracker.backend.dto.BodyMeasurementRequest;
import com.healthtracker.backend.dto.DailyRecordRequest;
import com.healthtracker.backend.dto.DailyRecordResponse;
import com.healthtracker.backend.dto.DashboardSummary;
import com.healthtracker.backend.dto.ExerciseSessionRequest;
import com.healthtracker.backend.dto.ExerciseSessionResponse;
import com.healthtracker.backend.dto.UserConfigRequest;
import com.healthtracker.backend.dto.UserConfigResponse;
import com.healthtracker.backend.model.BodyMeasurement;
import com.healthtracker.backend.model.DailyRecord;
import com.healthtracker.backend.model.ExerciseCategory;
import com.healthtracker.backend.model.ExerciseSession;
import com.healthtracker.backend.model.User;
import com.healthtracker.backend.model.UserConfig;
import com.healthtracker.backend.repository.BodyMeasurementRepository;
import com.healthtracker.backend.repository.DailyRecordRepository;
import com.healthtracker.backend.repository.ExerciseCategoryRepository;
import com.healthtracker.backend.repository.UserConfigRepository;
import com.healthtracker.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class HealthTrackerService {

    private final UserRepository userRepository;
    private final DailyRecordRepository dailyRecordRepository;
    private final BodyMeasurementRepository bodyMeasurementRepository;
    private final ExerciseCategoryRepository exerciseCategoryRepository;
    private final UserConfigRepository userConfigRepository;

    public HealthTrackerService(UserRepository userRepository,
                                DailyRecordRepository dailyRecordRepository,
                                BodyMeasurementRepository bodyMeasurementRepository,
                                ExerciseCategoryRepository exerciseCategoryRepository,
                                UserConfigRepository userConfigRepository) {
        this.userRepository = userRepository;
        this.dailyRecordRepository = dailyRecordRepository;
        this.bodyMeasurementRepository = bodyMeasurementRepository;
        this.exerciseCategoryRepository = exerciseCategoryRepository;
        this.userConfigRepository = userConfigRepository;
    }

    // ---------- Daily Records ----------

    public List<DailyRecordResponse> getRecords(Long userId) {
        return dailyRecordRepository.findByUserIdOrderByRecordDateDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<DailyRecordResponse> getRecordsBetween(Long userId, LocalDate start, LocalDate end) {
        return dailyRecordRepository.findByUserIdAndRecordDateBetweenOrderByRecordDateAsc(userId, start, end)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public DailyRecordResponse createRecord(Long userId, DailyRecordRequest request) {
        User user = userRepository.getReferenceById(userId);
        DailyRecord record = new DailyRecord();
        record.setUser(user);
        record.setRecordDate(request.recordDate());
        record.setWeightKg(request.weightKg());
        record.setBodyFatPct(request.bodyFatPct());
        record.setKcalConsumed(request.kcalConsumed());
        record.setKcalExpended(request.kcalExpended());
        record.setNotes(request.notes());

        if (request.sessions() != null) {
            for (ExerciseSessionRequest s : request.sessions()) {
                ExerciseCategory cat = exerciseCategoryRepository.getReferenceById(s.exerciseCategoryId());
                ExerciseSession session = new ExerciseSession();
                session.setExerciseCategory(cat);
                session.setDurationMin(s.durationMin());
                session.setNotes(s.notes());
                record.addSession(session);
            }
        }

        return toResponse(dailyRecordRepository.save(record));
    }

    public DailyRecordResponse updateRecord(Long recordId, Long userId, DailyRecordRequest request) {
        DailyRecord record = dailyRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Record not found"));

        if (!record.getUser().getId().equals(userId)) {
            throw new SecurityException("Access denied");
        }

        record.setRecordDate(request.recordDate());
        record.setWeightKg(request.weightKg());
        record.setBodyFatPct(request.bodyFatPct());
        record.setKcalConsumed(request.kcalConsumed());
        record.setKcalExpended(request.kcalExpended());
        record.setNotes(request.notes());

        record.getSessions().clear();

        if (request.sessions() != null) {
            for (ExerciseSessionRequest s : request.sessions()) {
                ExerciseCategory cat = exerciseCategoryRepository.getReferenceById(s.exerciseCategoryId());
                ExerciseSession session = new ExerciseSession();
                session.setExerciseCategory(cat);
                session.setDurationMin(s.durationMin());
                session.setNotes(s.notes());
                record.addSession(session);
            }
        }

        return toResponse(dailyRecordRepository.save(record));
    }

    public void deleteRecord(Long recordId, Long userId) {
        DailyRecord record = dailyRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Record not found"));
        if (!record.getUser().getId().equals(userId)) {
            throw new SecurityException("Access denied");
        }
        dailyRecordRepository.delete(record);
    }

    // ---------- Body Measurements ----------

    public List<BodyMeasurement> getMeasurements(Long userId) {
        return bodyMeasurementRepository.findByUserIdOrderByMeasurementDateDesc(userId);
    }

    public BodyMeasurement createMeasurement(Long userId, BodyMeasurementRequest request) {
        User user = userRepository.getReferenceById(userId);
        BodyMeasurement measurement = new BodyMeasurement();
        measurement.setUser(user);
        measurement.setMeasurementDate(request.measurementDate());
        measurement.setChestCm(request.chestCm());
        measurement.setWaistCm(request.waistCm());
        measurement.setHipsCm(request.hipsCm());
        measurement.setArmCm(request.armCm());
        measurement.setThighCm(request.thighCm());
        measurement.setNeckCm(request.neckCm());
        return bodyMeasurementRepository.save(measurement);
    }

    public void deleteMeasurement(Long measurementId, Long userId) {
        BodyMeasurement measurement = bodyMeasurementRepository.findById(measurementId)
                .orElseThrow(() -> new IllegalArgumentException("Measurement not found"));
        if (!measurement.getUser().getId().equals(userId)) {
            throw new SecurityException("Access denied");
        }
        bodyMeasurementRepository.delete(measurement);
    }

    // ---------- Exercise Categories ----------

    public List<ExerciseCategory> getCategories(Long userId) {
        return exerciseCategoryRepository.findByUserIdOrderByNameAsc(userId);
    }

    public ExerciseCategory createCategory(Long userId, String name) {
        User user = userRepository.getReferenceById(userId);
        ExerciseCategory category = new ExerciseCategory();
        category.setUser(user);
        category.setName(name);
        return exerciseCategoryRepository.save(category);
    }

    public void deleteCategory(Long categoryId, Long userId) {
        ExerciseCategory category = exerciseCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        if (!category.getUser().getId().equals(userId)) {
            throw new SecurityException("Access denied");
        }
        exerciseCategoryRepository.delete(category);
    }

    // ---------- User Config ----------

    public UserConfigResponse getUserConfig(Long userId) {
        return userConfigRepository.findByUserId(userId)
                .map(c -> new UserConfigResponse(c.getProvider(), c.getBaseUrl(), c.getDefaultModel()))
                .orElse(null);
    }

    public UserConfigResponse updateUserConfig(Long userId, UserConfigRequest request) {
        User user = userRepository.getReferenceById(userId);
        UserConfig config = userConfigRepository.findByUserId(userId)
                .orElseGet(() -> {
                    UserConfig newConfig = new UserConfig();
                    newConfig.setUser(user);
                    return newConfig;
                });

        config.setProvider(request.provider());
        config.setBaseUrl(request.baseUrl());
        config.setDefaultModel(request.defaultModel());
        if (request.apiKey() != null && !request.apiKey().isBlank()) {
            config.setApiKeyEncrypted(request.apiKey());
        }

        UserConfig saved = userConfigRepository.save(config);
        return new UserConfigResponse(saved.getProvider(), saved.getBaseUrl(), saved.getDefaultModel());
    }

    // ---------- Dashboard ----------

    public DashboardSummary getDashboardSummary(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusDays(7);

        List<DailyRecord> recent = dailyRecordRepository
                .findByUserIdAndRecordDateBetweenOrderByRecordDateAsc(userId, weekAgo, today);

        DailyRecord last = null;
        DailyRecord first = null;
        if (!recent.isEmpty()) {
            last = recent.getLast();
            first = recent.getFirst();
        }

        // If no data in last 7 days, try last available record
        if (last == null) {
            List<DailyRecord> all = dailyRecordRepository.findByUserIdOrderByRecordDateDesc(userId);
            if (!all.isEmpty()) {
                last = all.getFirst();
            }
        }

        String weight = last != null && last.getWeightKg() != null
                ? last.getWeightKg().setScale(1, RoundingMode.HALF_UP) + " kg" : "N/A";
        String bodyFat = last != null && last.getBodyFatPct() != null
                ? last.getBodyFatPct().setScale(1, RoundingMode.HALF_UP) + "%" : "N/A";

        String weightChange = "N/A";
        String bfChange = "N/A";
        if (last != null && first != null && last.getWeightKg() != null && first.getWeightKg() != null) {
            BigDecimal diff = last.getWeightKg().subtract(first.getWeightKg());
            weightChange = (diff.compareTo(BigDecimal.ZERO) <= 0 ? "" : "+")
                    + diff.setScale(1, RoundingMode.HALF_UP) + " kg";
        }
        if (last != null && first != null && last.getBodyFatPct() != null && first.getBodyFatPct() != null) {
            BigDecimal diff = last.getBodyFatPct().subtract(first.getBodyFatPct());
            bfChange = (diff.compareTo(BigDecimal.ZERO) <= 0 ? "" : "+")
                    + diff.setScale(1, RoundingMode.HALF_UP) + " %";
        }

        String avgKcalIn = recent.stream()
                .filter(r -> r.getKcalConsumed() != null)
                .mapToInt(DailyRecord::getKcalConsumed)
                .average()
                .stream()
                .mapToObj(avg -> Math.round(avg) + " kcal")
                .findFirst().orElse("N/A");

        String avgKcalOut = recent.stream()
                .filter(r -> r.getKcalExpended() != null)
                .mapToInt(DailyRecord::getKcalExpended)
                .average()
                .stream()
                .mapToObj(avg -> Math.round(avg) + " kcal")
                .findFirst().orElse("N/A");

        List<Map<String, Object>> weekly = new ArrayList<>();
        for (DailyRecord r : recent) {
            Map<String, Object> day = new HashMap<>();
            day.put("date", r.getRecordDate().toString());
            day.put("weight", r.getWeightKg());
            day.put("bodyFat", r.getBodyFatPct());
            day.put("kcalConsumed", r.getKcalConsumed());
            day.put("kcalExpended", r.getKcalExpended());
            weekly.add(day);
        }

        return new DashboardSummary(weight, bodyFat, weightChange, bfChange, avgKcalIn, avgKcalOut, weekly);
    }

    // ---------- Helpers ----------

    private DailyRecordResponse toResponse(DailyRecord record) {
        List<ExerciseSessionResponse> sessions = record.getSessions().stream()
                .map(s -> new ExerciseSessionResponse(
                        s.getExerciseCategory().getId(),
                        s.getExerciseCategory().getName(),
                        s.getDurationMin(),
                        s.getNotes()))
                .toList();

        return new DailyRecordResponse(
                record.getId(),
                record.getRecordDate(),
                record.getWeightKg(),
                record.getBodyFatPct(),
                record.getKcalConsumed(),
                record.getKcalExpended(),
                sessions,
                record.getExerciseDurationMin(),
                record.getNotes()
        );
    }
}
