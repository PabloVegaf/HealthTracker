package com.healthtracker.backend.repository;

import com.healthtracker.backend.model.DailyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyRecordRepository extends JpaRepository<DailyRecord, Long> {

    List<DailyRecord> findByUserIdOrderByRecordDateDesc(Long userId);

    List<DailyRecord> findByUserIdAndRecordDateBetweenOrderByRecordDateAsc(
            Long userId, LocalDate start, LocalDate end);

    Optional<DailyRecord> findByUserIdAndRecordDate(Long userId, LocalDate recordDate);

    boolean existsByUserIdAndRecordDate(Long userId, LocalDate recordDate);
}
