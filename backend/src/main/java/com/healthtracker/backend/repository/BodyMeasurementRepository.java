package com.healthtracker.backend.repository;

import com.healthtracker.backend.model.BodyMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
/**
 * Repositorio de mediciones corporales por usuario y fecha.
 */
public interface BodyMeasurementRepository extends JpaRepository<BodyMeasurement, Long> {

    List<BodyMeasurement> findByUserIdOrderByMeasurementDateDesc(Long userId);

    Optional<BodyMeasurement> findByUserIdAndMeasurementDate(Long userId, java.time.LocalDate measurementDate);

    boolean existsByUserIdAndMeasurementDate(Long userId, java.time.LocalDate measurementDate);
}
