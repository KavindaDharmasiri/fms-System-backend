package net.com.fms_core.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationClient {

    private final RestTemplate restTemplate = new RestTemplate();
    
    @Value("${notification.service.url:http://localhost:8086}")
    private String notificationServiceUrl;

    public void sendHighRiskAlert(String transactionId, String riskScore, String amount, String cardNumber) {
        try {
            String url = notificationServiceUrl + "/api/v1/notifications/high-risk" +
                    "?transactionId=" + transactionId +
                    "&riskScore=" + riskScore +
                    "&amount=" + amount +
                    "&cardNumber=" + cardNumber;
            
            ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
            log.info("High risk notification sent for transaction: {}", transactionId);
        } catch (Exception e) {
            log.error("Failed to send high risk notification: {}", e.getMessage());
        }
    }

    public void sendMediumRiskAlert(String transactionId, String riskScore, String amount, String cardNumber) {
        try {
            String url = notificationServiceUrl + "/api/v1/notifications/medium-risk" +
                    "?transactionId=" + transactionId +
                    "&riskScore=" + riskScore +
                    "&amount=" + amount +
                    "&cardNumber=" + cardNumber;
            
            ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
            log.info("Medium risk notification sent for transaction: {}", transactionId);
        } catch (Exception e) {
            log.error("Failed to send medium risk notification: {}", e.getMessage());
        }
    }
}