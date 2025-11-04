/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package com.example.proj2.service;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import java.util.concurrent.CompletableFuture;
import java.lang.reflect.Type;
/**
@Service
public class ClientService {
    private final WebSocketStompClient stompClient;
    public ClientService(WebSocketStompClient stompClient) {
        this.stompClient = stompClient;
    }
    public void connectToServer() {
        String url = "ws://localhost:8084/fms-core-service/ws/websocket";
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
                        session.send("/app/send", "0400F66464810CE0A01E000000420000000010489011612928660400000000000013540000000013540004111856456100000009300027105814014401005906464572F3F1F0F1F0F7F0F9F3F0F0F0D7E6C8D3D8E4F9F9F9F9F9F9F9F9F0F3F7F0F0F8F2F7F6F0F0F1404040E4C2C5D940C5C1E3E240404040404040404040404040404040F8F5F240404040404040404040D3D2014401440601000000070212000000000000000000000000000000103400104000000000000000030310128604299007A0000000022504010009300004110756440000046457200000000000F0F0F0F0F0F0F1F0F3F4F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0");
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
