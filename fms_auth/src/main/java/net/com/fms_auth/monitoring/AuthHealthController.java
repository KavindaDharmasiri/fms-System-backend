package net.com.fms_auth.monitoring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/health")
public class AuthHealthController {

    private final DataSource dataSource;

    public AuthHealthController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping
    public Map<String, Object> getHealth() {
        Map<String, Object> health = new HashMap<>();
        
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(1)) {
                health.put("status", "UP");
                health.put("database", "Available");
                health.put("service", "fms-auth");
            } else {
                health.put("status", "DOWN");
                health.put("database", "Connection validation failed");
            }
        } catch (Exception e) {
            health.put("status", "DOWN");
            health.put("database", "Unavailable");
            health.put("error", e.getMessage());
        }
        
        return health;
    }
}