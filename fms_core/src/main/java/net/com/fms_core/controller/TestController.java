/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.controller;

import lombok.RequiredArgsConstructor;
import net.com.fms_core.config.SocketListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    
    private final SocketListener socketListener;
    
    @PostMapping("/socket-message")
    public ResponseEntity<String> testSocketMessage(@RequestBody String hexMessage) {
        try {
            String result = socketListener.handleMessage(hexMessage.trim());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}