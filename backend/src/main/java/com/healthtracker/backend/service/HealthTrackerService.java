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

@Service
@Transactional
public class HealthTrackerService {

    // Repositories
    private final UserRepository userRepository;
    private final DailyRecordRepository dailyRecordRepository;
    private final BodyMeasurementRepository bodyMeasurementRepository;
    private final ExerciseCategoryRepository exerciseCategoryRepository;
    private final UserConfigRepository userConfigRepository;

    // Service methods
    public HealthTrackerService(
        UserRepository userRepository,
        DailyRecordRepository dailyRecordRepository,
        BodyMeasurementRepository bodyMeasurementRepository,
        ExerciseCategoryRepository exerciseCategoryRepository,
        UserConfigRepository userConfigRepository
    ) {
        this.userRepository = userRepository;
        this.dailyRecordRepository = dailyRecordRepository;
        this.bodyMeasurementRepository = bodyMeasurementRepository;
        this.exerciseCategoryRepository = exerciseCategoryRepository;
        this.userConfigRepository = userConfigRepository;
    }
}
