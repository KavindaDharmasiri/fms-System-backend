/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_auth.dto.common.StandardResponse;
import net.com.fms_auth.dto.request.SysUserFilterDTO;
import net.com.fms_auth.dto.request.SystemUserDto;
import net.com.fms_auth.dto.response.SysUserResponseDTO;
import net.com.fms_auth.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    @PostMapping("/add-system-user")
    public ResponseEntity<StandardResponse> addSystemUser(@RequestBody SystemUserDto systemUserDto){
        return userService.addSystemUser(systemUserDto);
    }
    @PostMapping("/get-all-users")
    public ResponseEntity<StandardResponse> getAllSystemUsers(@RequestBody SysUserFilterDTO sysUserFilterDTO){
        return userService.getAllSystemUsers(sysUserFilterDTO);
    }
        @GetMapping("/get-user-byId")
    public ResponseEntity<StandardResponse> getUserById(@RequestParam(required = true) Integer userId){
        return userService.getUserById(userId);
    }
    @DeleteMapping("/delete-by-id")
    public ResponseEntity<StandardResponse> deleteSystemUserById(@RequestParam(required = true) Integer userId){
        return userService.deleteUserById(userId);
    }
    @GetMapping("/get-all-users-with-username")
    public ResponseEntity getAllUsersWithUsername(){
        return userService.getAllUsersWithUsername();
    }
}
