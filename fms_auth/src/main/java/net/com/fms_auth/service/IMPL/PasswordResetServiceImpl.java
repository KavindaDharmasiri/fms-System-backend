/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.service.IMPL;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_auth.dto.APIResponseDTO;
import net.com.fms_auth.dto.CommonResponseDTO;
import net.com.fms_auth.dto.request.PasswordResetRequestDTO;
import net.com.fms_auth.entity.PasswordResetRequest;
import net.com.fms_auth.entity.User;
import net.com.fms_auth.exceptions.AlreadyReportedException;
import net.com.fms_auth.repository.PasswordResetRequestRepository;
import net.com.fms_auth.repository.UserRepository;
import net.com.fms_auth.service.PasswordResetService;
import net.com.fms_auth.util.EmailSender;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {
    private static final String PENDING_REQUEST = "PENDING_REQUEST";
    private static final String REQUEST_EXPIRED = "REQUEST_EXPIRED";
    private static final String VALID_REQUEST_KEY = "VALID_REQUEST_KEY";
    private static final String COMPLETED = "COMPLETED";
    private static final String INVALID_KEY_OR_STATUS = "INVALID_KEY_OR_STATUS";
    private static final String BASE_URL = "BASE_URL";
    private static final String PUBLIC_REDIRECT_PATH = "resetPassword-Self";
    private static final String TEMPLATE_NAME = "reset";
    private static final int REQUEST_TIMEOUT_MINUTES = 15;
    private final PasswordResetRequestRepository passwordResetRequestRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private String passwordResetReturnURL;
    @Override
    public ResponseEntity initRequest(String username) {
        Optional<User> optionalOAuthUser = userRepository.findByUsernameEquals(username);
        if (!optionalOAuthUser.isPresent()) {
            throw new UsernameNotFoundException("User not found.");
        }
        User user = optionalOAuthUser.get();
        PasswordResetRequest passwordResetRequest = fetchPendingRequest(user.getUsername());
        if (passwordResetRequest != null) {
            if (getTimeDifference(passwordResetRequest.getCreatedTime(), new Date()) < REQUEST_TIMEOUT_MINUTES * 60 * 1000) {
                throw new AlreadyReportedException("Already has a request");
            } else {
                passwordResetRequest.setRequestStatus(REQUEST_EXPIRED);
                passwordResetRequestRepository.save(passwordResetRequest);
            }
        }
        PasswordResetRequest newRequest = initiateNewRequest(username);
        try {
            String resetURL = buildResetURL(newRequest.getRequestKey());
            String message = "<h3>Dear " + user.getUsername() + ",<br /> Your Account Password Reset Request is Here! <a href='" + resetURL + "'>RESET PASSWORD</a>!</h3><br />Ceylinco Team!";
            EmailSender.sendMail("Password Reset fms", message, username);
            return new ResponseEntity<>(new APIResponseDTO(true,"Password reset email sent"), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.toString());
            passwordResetRequestRepository.deleteById(newRequest.getId());
            return new ResponseEntity<>(new APIResponseDTO(false,"COMMUNICATION_ERROR"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Override
    public ResponseEntity<CommonResponseDTO> verifyToken(String resetKey) {
        String status = verify(resetKey);
        if (status.equals(VALID_REQUEST_KEY)) {
            return new ResponseEntity<>(new CommonResponseDTO(VALID_REQUEST_KEY, true), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new CommonResponseDTO(status, false), HttpStatus.OK);
        }
    }
    @Override
    public ResponseEntity<CommonResponseDTO> resetPassword(PasswordResetRequestDTO resetRequestDTO) {
        String status = verify(resetRequestDTO.getResetKey());
        if (!status.equals(VALID_REQUEST_KEY)) {
            return new ResponseEntity<>(new CommonResponseDTO(status, false), HttpStatus.EXPECTATION_FAILED);
        }
        PasswordResetRequest resetRequest = passwordResetRequestRepository.findByRequestKey(resetRequestDTO.getResetKey());
        String userName = resetRequest.getUsername();
        Optional<User> oAuthUser = userRepository.findByUsernameEquals(userName);
        if (!oAuthUser.isPresent()) {
            return new ResponseEntity<>(new CommonResponseDTO("Invalid Username", false), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        User user = oAuthUser.get();
        user.setPassword(passwordEncoder.encode(resetRequestDTO.getPassword()));
        userRepository.save(user);
        resetRequest.setRequestStatus(COMPLETED);
        passwordResetRequestRepository.save(resetRequest);
        return new ResponseEntity<>(new CommonResponseDTO(COMPLETED, true), HttpStatus.OK);
    }
    @Override
    public ResponseEntity<CommonResponseDTO> resetWhenLoggedIn(PasswordResetRequestDTO passwordResetRequestDTO) {
        return null;
    }
    private String verify(String resetKey) {
        PasswordResetRequest passwordResetRequest = passwordResetRequestRepository.findByRequestKey(resetKey);
        if (passwordResetRequest != null && passwordResetRequest.getRequestStatus().equals(PENDING_REQUEST)) {
            long timeDifference = getTimeDifference(passwordResetRequest.getCreatedTime(), new Date());
            if (timeDifference > REQUEST_TIMEOUT_MINUTES * 60 * 1000) {
                passwordResetRequest.setRequestStatus(REQUEST_EXPIRED);
                passwordResetRequestRepository.save(passwordResetRequest);
                return REQUEST_EXPIRED;
            } else {
                return VALID_REQUEST_KEY;
            }
        } else {
            return INVALID_KEY_OR_STATUS;
        }
    }
    private PasswordResetRequest fetchPendingRequest(String userName) {
        return passwordResetRequestRepository.findByUsernameAndRequestStatus(userName, PENDING_REQUEST);
    }
    public static long getTimeDifference(Date start, Date end) {
        return Duration.between(start.toInstant(), end.toInstant()).toMillis();
    }
    private PasswordResetRequest initiateNewRequest(String userName) {
        PasswordResetRequest request = new PasswordResetRequest();
        String key = UUID.randomUUID().toString();
        request.setUsername(userName);
        request.setRequestKey(key);
        request.setRequestStatus(PENDING_REQUEST);
        request.setCreatedTime(new Date());
        return passwordResetRequestRepository.save(request);
    }
    private String buildResetURL(String key) throws Exception {
        String baseURL = "http://192.168.2.105:4200/passwordreset";
        return baseURL.concat("?request_id=").concat(key);
    }
}
