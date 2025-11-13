package net.com.fms_core.monitoring;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
public class DatabaseHealthIndicator {

    private final DataSource dataSource;

    public DatabaseHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, Object> checkHealth() {
        Map<String, Object> health = new HashMap<>();
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(1)) {
                health.put("status", "UP");
                health.put("database", "Available");
                health.put("validationQuery", "SELECT 1");
            } else {
                health.put("status", "DOWN");
                health.put("database", "Connection validation failed");
            }
        } catch (SQLException e) {
            health.put("status", "DOWN");
            health.put("database", "Unavailable");
            health.put("error", e.getMessage());
        }
        return health;
    }
}