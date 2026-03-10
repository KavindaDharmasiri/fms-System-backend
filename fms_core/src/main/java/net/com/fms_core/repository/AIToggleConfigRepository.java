package net.com.fms_core.repository;

import net.com.fms_core.entity.AIToggleConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AIToggleConfigRepository extends JpaRepository<AIToggleConfig, Long> {
    Optional<AIToggleConfig> findByConfigKey(String configKey);
}