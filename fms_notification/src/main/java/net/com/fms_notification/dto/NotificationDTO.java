package net.com.fms_notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private String id;
    private String transactionId;
    private String riskLevel; // HIGH, MEDIUM, LOW
    private String riskScore;
    private String amount;
    private String cardNumber;
    private String merchantName;
    private String message;
    private LocalDateTime timestamp;
    private String status; // PENDING, SENT, FAILED
}