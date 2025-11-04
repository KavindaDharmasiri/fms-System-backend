/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.service;
import net.com.fms_core.dto.FilterDualAuthentication;
import net.com.fms_core.dto.RejectDualAuthDTO;
import org.springframework.http.ResponseEntity;
public interface DualAuthenticationService {
    ResponseEntity getAllPendingApproveRequests(FilterDualAuthentication filterDualAuthentication);
    ResponseEntity approveRequest(String identifier);
    ResponseEntity rejectRequest(RejectDualAuthDTO rejectDualAuthDTO);
    ResponseEntity getRequestByIdentifier(String identifier);
}
