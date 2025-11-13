package net.com.fms_core.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfiguration {

    @Bean
    public Counter fraudDetectionCounter(MeterRegistry meterRegistry) {
        return Counter.builder("fraud_detection_total")
            .description("Total number of fraud detection attempts")
            .register(meterRegistry);
    }

    @Bean
    public Counter fraudAlertsCounter(MeterRegistry meterRegistry) {
        return Counter.builder("fraud_alerts_total")
            .description("Total number of fraud alerts generated")
            .register(meterRegistry);
    }

    @Bean
    public Timer transactionProcessingTimer(MeterRegistry meterRegistry) {
        return Timer.builder("transaction_processing_duration")
            .description("Time taken to process transactions")
            .register(meterRegistry);
    }

    @Bean
    public Counter auditLogsCounter(MeterRegistry meterRegistry) {
        return Counter.builder("audit_logs_total")
            .description("Total number of audit logs created")
            .register(meterRegistry);
    }
}