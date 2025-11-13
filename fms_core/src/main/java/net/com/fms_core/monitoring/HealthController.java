package net.com.fms_core.monitoring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

    private final DatabaseHealthIndicator databaseHealth;
    private final FraudDetectionHealthIndicator fraudHealth;

    public HealthController(DatabaseHealthIndicator databaseHealth, 
                          FraudDetectionHealthIndicator fraudHealth) {
        this.databaseHealth = databaseHealth;
        this.fraudHealth = fraudHealth;
    }

    @GetMapping
    public Map<String, Object> getHealth() {
        Map<String, Object> health = new HashMap<>();
        
        Map<String, Object> dbHealth = databaseHealth.checkHealth();
        Map<String, Object> fraudEngineHealth = fraudHealth.checkHealth();
        
        boolean allUp = "UP".equals(dbHealth.get("status")) && 
                       "UP".equals(fraudEngineHealth.get("status"));
        
        health.put("status", allUp ? "UP" : "DOWN");
        health.put("components", Map.of(
            "database", dbHealth,
            "fraudEngine", fraudEngineHealth
        ));
        
        return health;
    }
}