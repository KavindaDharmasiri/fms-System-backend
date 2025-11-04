/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package com.example.proj1.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
/**
@Controller
public class MessageController {
    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public String handleMessage(String message) {
        System.out.println("Received message: "+ message);
        return "Project A received: " + message;
    }
}
