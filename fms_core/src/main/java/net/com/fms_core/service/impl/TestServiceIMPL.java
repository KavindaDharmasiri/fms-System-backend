/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.service.impl;
import lombok.RequiredArgsConstructor;
import net.com.fms_core.service.TestService;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;

@Service("testService")
@RequiredArgsConstructor
public class TestServiceIMPL implements TestService {
    private final WebSocketStompClient stompClient;
    public void connectToServer() {
        String url = "ws://localhost:8080/ws/websocket";
        CompletableFuture<StompSession> future = stompClient.connectAsync(
                url,
                new StompSessionHandlerAdapter() {
                    @Override
                    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                        session.subscribe("/topic/messages", new StompFrameHandler() {
                            @Override
                            public Type getPayloadType(StompHeaders headers) {
                                return String.class; // Specify the payload type (e.g., String, JSON, etc.)
                            }
                            @Override
                            public void handleFrame(StompHeaders headers, Object payload) {
                                System.out.println("Received: " + payload); // Handle the message
                            }
                        });
                        for (int i = 0; i < 10; i++) {
                            session.send("/app/send", "Hello from fms Core Service : " + i);
                        }
                    }
                }
        );
        future.thenAccept(session -> {
            System.out.println("Connected successfully!");
        }).exceptionally(ex -> {
            System.err.println("Connection failed: " + ex.getMessage());
            return null;
        });
    }
}
