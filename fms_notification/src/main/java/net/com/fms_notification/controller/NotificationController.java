package net.com.fms_notification.controller;

import lombok.RequiredArgsConstructor;
import net.com.fms_notification.dto.NotificationDTO;
import net.com.fms_notification.entity.Notification;
import net.com.fms_notification.entity.NotificationStatus;
import net.com.fms_notification.repository.NotificationRepository;
import net.com.fms_notification.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;

    @PostMapping("/risk-alert")
    public ResponseEntity<String> sendRiskAlert(@RequestBody NotificationDTO notification) {
        notificationService.sendRiskNotification(notification);
        return ResponseEntity.ok("Notification sent successfully");
    }

    @PostMapping("/high-risk")
    public ResponseEntity<String> sendHighRiskAlert(
            @RequestParam String transactionId,
            @RequestParam String riskScore,
            @RequestParam String amount,
            @RequestParam String cardNumber) {
        
        notificationService.sendHighRiskAlert(transactionId, riskScore, amount, cardNumber);
        return ResponseEntity.ok("High risk alert sent");
    }

    @PostMapping("/medium-risk")
    public ResponseEntity<String> sendMediumRiskAlert(
            @RequestParam String transactionId,
            @RequestParam String riskScore,
            @RequestParam String amount,
            @RequestParam String cardNumber) {
        
        notificationService.sendMediumRiskAlert(transactionId, riskScore, amount, cardNumber);
        return ResponseEntity.ok("Medium risk alert sent");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable String userId) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/{id}/read")
    @Transactional
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        try {
            notificationRepository.updateStatus(id, NotificationStatus.READ);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
