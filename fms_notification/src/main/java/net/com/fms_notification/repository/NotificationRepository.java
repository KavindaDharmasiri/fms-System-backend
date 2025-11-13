package net.com.fms_notification.repository;

import net.com.fms_notification.entity.Notification;
import net.com.fms_notification.entity.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(String userId);
    
    @Modifying
    @Query("UPDATE Notification n SET n.status = :status WHERE n.id = :id")
    void updateStatus(Long id, NotificationStatus status);
}