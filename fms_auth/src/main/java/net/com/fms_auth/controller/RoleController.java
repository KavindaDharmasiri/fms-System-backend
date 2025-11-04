/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_auth.dto.PrivilegesDTO;
import net.com.fms_auth.dto.RoleDTO;
import net.com.fms_auth.dto.common.CommonPageReqDTO;
import net.com.fms_auth.dto.common.GetAllRolesRequestDTO;
import net.com.fms_auth.service.RoleService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@Slf4j
public class RoleController {
    private final RoleService roleService;
    @PostMapping("/add-role")
    public ResponseEntity addRole(@RequestBody RoleDTO roleDTO){
        return roleService.addRole(roleDTO);
    }
    @PostMapping("/get-all-roles")
    public ResponseEntity getAllRoles(@RequestBody GetAllRolesRequestDTO getAllRolesRequestDTO){
        return roleService.getAllRoles(getAllRolesRequestDTO);
    }
    @GetMapping("/get-roll-by-id")
    public ResponseEntity getRollByID(@RequestParam("roleID")int roleID ){
        return roleService.getRollByID(roleID);
    }
    @DeleteMapping("/delete-roll-by-id")
    public ResponseEntity deleteRoleByID(@RequestParam("roleID")int roleID){
        return roleService.deleteRoleByID(roleID);
    }
    @GetMapping("/get-privileges-by-role-id")
    public ResponseEntity getPrivilegesByRoleID(@RequestParam("roleID")int roleID){
        return roleService.getPrivilegesByRoleID(roleID);
    }
    @PutMapping("/update-privilege")
    public ResponseEntity updateRolePrivileges(@RequestBody PrivilegesDTO privilegesDTO) {
        return roleService.updateRolePrivileges(privilegesDTO);
    }
    @GetMapping("/get-all-role-names")
    public ResponseEntity getAllRoleNames(){
        return roleService.getAllRoleNames();
    }
    @GetMapping("/get-all-page-names")
    public ResponseEntity getAllPageNames(){
        return roleService.getAllPageNames();
    }
    @GetMapping("/get-all-section-names")
    public ResponseEntity getAllSectionNames(){
        return roleService.getAllSectionNames();
    }
    @GetMapping("/get-all-task-names")
    public ResponseEntity getAllTaskNames(){
        return roleService.getAllTaskNames();
    }
}
