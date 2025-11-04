/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.config;
import net.com.fms_core.dto.TransactionHistoryDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class SseConfig {
    @Bean
    public Sinks.Many<TransactionHistoryDTO> transactionSink() {
        return Sinks.many().multicast().onBackpressureBuffer();
    }
}
