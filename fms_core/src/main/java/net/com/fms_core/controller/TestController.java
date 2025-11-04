/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.dto.ApiResponseDTO;
import net.com.fms_core.dto.ErrorDetailDTO;
import net.com.fms_core.enums.ErrorCode;
import net.com.fms_core.service.impl.TestServiceIMPL;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v3/test")
public class TestController {
    private final TestServiceIMPL testService;
    @GetMapping("/success")
    public ResponseEntity<ApiResponseDTO> test() {
        return ResponseEntity.ok(ApiResponseDTO.success("test"));
    }
    @GetMapping("/error")
    public ResponseEntity<ApiResponseDTO> error() {
        return ResponseEntity.status(500).body(ApiResponseDTO.error(ErrorDetailDTO.builder().code(ErrorCode.INTERNAL_SERVER_ERROR).message("An unexpected error occurred.").field("test").build()));
    }
    @GetMapping("/errors")
    public ResponseEntity<ApiResponseDTO> errors() {
        return ResponseEntity.status(500).body(ApiResponseDTO.error(
                List.of(
                        ErrorDetailDTO.builder().code(ErrorCode.DUPLICATE_ENTRY).message("Email already exists").field("email").build(),
                        ErrorDetailDTO.builder().code(ErrorCode.INVALID_INPUT).message("Password is mandatory").field("password").build(),
                        ErrorDetailDTO.builder().code(ErrorCode.INVALID_INPUT).message("Name is mandatory").field("name").build()
                )
        ));
    }
    @PostMapping("/ws-test")
    public ResponseEntity<ApiResponseDTO> wsTest() {
        testService.connectToServer();
        return ResponseEntity.ok(ApiResponseDTO.success("ws-test"));
    }
}
