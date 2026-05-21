package com.healthtracker.backend.repository;

import com.healthtracker.backend.model.UserConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
/**
 * Repositorio de configuración de proveedor IA por usuario.
 */
public interface UserConfigRepository extends JpaRepository<UserConfig, Long> {
    Optional<UserConfig> findByUserId(Long userId);
}
