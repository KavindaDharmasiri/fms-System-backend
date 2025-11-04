/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_auth.dto.request.LoginDTO;
import net.com.fms_auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDTO loginDTO){
        return authService.login(loginDTO);
    }
    @GetMapping("/test")
    @PreAuthorize("hasAuthority('LOGIN')")
    public String test(){
        return "teset";
    }
    
    @GetMapping("/me")
    public ResponseEntity me(){
        return authService.getCurrentUser();
    }
}
