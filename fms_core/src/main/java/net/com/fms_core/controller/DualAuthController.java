/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.controller;
import jakarta.ws.rs.POST;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.dto.FilterDualAuthentication;
import net.com.fms_core.dto.RejectDualAuthDTO;
import net.com.fms_core.dto.test.TestCompareDTO;
import net.com.fms_core.service.DualAuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dual-auth")
public class DualAuthController {
    private final DualAuthenticationService dualAuthenticationService;
    @PostMapping("/get-all")
    public ResponseEntity getAllPendingApproveRequests(@RequestBody FilterDualAuthentication filterDualAuthentication){
        return dualAuthenticationService.getAllPendingApproveRequests(filterDualAuthentication);
    }
    @PostMapping("/approve-request")
    public ResponseEntity approveRequest(@RequestParam("identifier") String identifier){
        return dualAuthenticationService.approveRequest(identifier);
    }
    @PostMapping("/reject-request")
    public ResponseEntity rejectRequest(@RequestBody RejectDualAuthDTO rejectDualAuthDTO){
        return dualAuthenticationService.rejectRequest(rejectDualAuthDTO);
    }
    @GetMapping("/get-request-by-identifier")
    public ResponseEntity getRequestByIdentifier(@RequestParam("identifier") String identifier){
        return dualAuthenticationService.getRequestByIdentifier(identifier);
    }
}
