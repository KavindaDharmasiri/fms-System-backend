/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.controller;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/stream")
@CrossOrigin(origins = "*") // Allow Angular to connect
public class StreamController {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    @GetMapping(value = "/loop", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamEvents(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");
        SseEmitter emitter = new SseEmitter();
        CompletableFuture.runAsync(() -> {
            try {
                for (int i = 0; i < 50; i++) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("message", "Trans_" + i+1);
                    emitter.send(SseEmitter.event()
                            .name("loopEvent")
                            .data(data));
                    Thread.sleep(300);
                }
                emitter.complete();
            } catch (Exception e) {
                e.printStackTrace();
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }
}
