package net.com.fms_core.controller;

import lombok.RequiredArgsConstructor;
import net.com.fms_core.service.AuditService;
import net.com.fms_core.util.AuthorizedUserContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
@CrossOrigin("*")
@ConditionalOnBean(AuditService.class)
public class FrontendAuditController {
    
    private final AuditService auditService;
    
    @PostMapping("/log-frontend-action")
    public ResponseEntity<String> logFrontendAction(@RequestBody Map<String, Object> auditData, HttpServletRequest request) {
        try {
            String userId = AuthorizedUserContext.getUser();
            if (userId == null) {
                userId = "ANONYMOUS";
            }
            
            // Create enhanced audit data with request info
            Map<String, Object> enhancedDetails = new HashMap<>();
            enhancedDetails.put("originalDetails", auditData.get("details"));
            enhancedDetails.put("url", auditData.get("url"));
            enhancedDetails.put("userAgent", request.getHeader("User-Agent"));
            enhancedDetails.put("ipAddress", getClientIpAddress(request));
            enhancedDetails.put("sessionId", request.getSession().getId());
            enhancedDetails.put("timestamp", auditData.get("timestamp"));
            
//            auditService.logActionWithRequestInfo(
//                userId,
//                (String) auditData.get("action"),
//                (String) auditData.get("entityType"),
//                (String) auditData.get("entityId"),
//                null,
//                enhancedDetails,
//                "SUCCESS",
//                null,
//                getClientIpAddress(request),
//                request.getHeader("User-Agent"),
//                request.getSession().getId()
//            );
            
            return ResponseEntity.ok("Audit logged successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to log audit: " + e.getMessage());
        }
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        return request.getRemoteAddr();
    }
}
