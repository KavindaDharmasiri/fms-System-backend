/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@Configuration
public class WebSocketClientConfig {
    @Bean
    public WebSocketClient webSocketClient() {
        return new StandardWebSocketClient();
    }
    @Bean
    public WebSocketStompClient stompClient(WebSocketClient webSocketClient) {
        WebSocketStompClient client = new WebSocketStompClient(webSocketClient);
        client.setMessageConverter(new StringMessageConverter());
        return client;
    }
}
