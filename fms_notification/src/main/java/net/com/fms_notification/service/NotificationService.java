package net.com.fms_notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_notification.dto.NotificationDTO;
import net.com.fms_notification.entity.Notification;
import net.com.fms_notification.entity.NotificationType;
import net.com.fms_notification.repository.NotificationRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;

    @Transactional
    public void sendRiskNotification(NotificationDTO notification) {
        try {
            // Save to database first
            Notification dbNotification = new Notification(
                "Kavinda Gimhan", // userId - could be extracted from context
                notification.getRiskLevel() + " Risk Alert",
                notification.getMessage(),
                NotificationType.FRAUD_ALERT
            );
            Notification saved = notificationRepository.save(dbNotification);
            
            // Update DTO with saved ID
            notification.setId(saved.getId().toString());
            notification.setTimestamp(LocalDateTime.now());
            notification.setStatus("SENT");

            // Send to WebSocket topics
            String topic = "/topic/risk-" + notification.getRiskLevel().toLowerCase();
            messagingTemplate.convertAndSend(topic, notification);
            messagingTemplate.convertAndSend("/topic/notifications", notification);
            
            log.info("Risk notification saved and sent: {} - {}", notification.getRiskLevel(), notification.getTransactionId());
        } catch (Exception e) {
            log.error("Failed to send notification: {}", e.getMessage());
            notification.setStatus("FAILED");
        }
    }

    public void sendHighRiskAlert(String transactionId, String riskScore, String amount, String cardNumber) {
        NotificationDTO notification = new NotificationDTO();
        notification.setTransactionId(transactionId);
        notification.setRiskLevel("HIGH");
        notification.setRiskScore(riskScore);
        notification.setAmount(amount);
        notification.setCardNumber(maskCardNumber(cardNumber));
        notification.setMessage("HIGH RISK TRANSACTION DETECTED - Immediate attention required");
        
        sendRiskNotification(notification);
    }

    public void sendMediumRiskAlert(String transactionId, String riskScore, String amount, String cardNumber) {
        NotificationDTO notification = new NotificationDTO();
        notification.setTransactionId(transactionId);
        notification.setRiskLevel("MEDIUM");
        notification.setRiskScore(riskScore);
        notification.setAmount(amount);
        notification.setCardNumber(maskCardNumber(cardNumber));
        notification.setMessage("Medium risk transaction detected - Review recommended");
        
        sendRiskNotification(notification);
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) return "****";
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }
}
