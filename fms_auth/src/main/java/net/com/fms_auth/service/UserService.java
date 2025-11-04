/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.service;
import net.com.fms_auth.dto.common.StandardResponse;
import net.com.fms_auth.dto.request.SysUserFilterDTO;
import net.com.fms_auth.dto.request.SystemUserDto;
import org.springframework.http.ResponseEntity;
public interface UserService {
    ResponseEntity<StandardResponse> addSystemUser(SystemUserDto systemUserDto);
    ResponseEntity<StandardResponse> getAllSystemUsers(SysUserFilterDTO sysUserFilterDTO);
    ResponseEntity<StandardResponse> deleteUserById(Integer userId);
    ResponseEntity<StandardResponse> getUserById(Integer userId);
    ResponseEntity getAllUsersWithUsername();
}
