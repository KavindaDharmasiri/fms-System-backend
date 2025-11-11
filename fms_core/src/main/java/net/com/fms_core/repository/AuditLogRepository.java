package net.com.fms_core.repository;

import net.com.fms_core.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    
    Page<AuditLog> findByUserIdOrderByTimestampDesc(String userId, Pageable pageable);
    
    Page<AuditLog> findByEntityTypeOrderByTimestampDesc(String entityType, Pageable pageable);
    
    Page<AuditLog> findByActionOrderByTimestampDesc(String action, Pageable pageable);
    
    @Query("SELECT a FROM AuditLog a WHERE a.timestamp BETWEEN :startDate AND :endDate ORDER BY a.timestamp DESC")
    Page<AuditLog> findByTimestampBetween(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate, 
                                         Pageable pageable);
    
    @Query("SELECT a FROM AuditLog a WHERE a.userId = :userId AND a.entityType = :entityType AND a.timestamp BETWEEN :startDate AND :endDate ORDER BY a.timestamp DESC")
    List<AuditLog> findUserActivityByEntityAndDateRange(@Param("userId") String userId,
                                                       @Param("entityType") String entityType,
                                                       @Param("startDate") LocalDateTime startDate,
                                                       @Param("endDate") LocalDateTime endDate);
}