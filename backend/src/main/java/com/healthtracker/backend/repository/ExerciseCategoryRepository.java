package com.healthtracker.backend.repository;

import com.healthtracker.backend.model.ExerciseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
/**
 * Repositorio de categorías de ejercicio asociadas a cada usuario.
 */
public interface ExerciseCategoryRepository extends JpaRepository<ExerciseCategory, Long> {

    List<ExerciseCategory> findByUserIdOrderByNameAsc(Long userId);

    Optional<ExerciseCategory> findByUserIdAndName(Long userId, String name);

    boolean existsByUserIdAndName(Long userId, String name);
}
