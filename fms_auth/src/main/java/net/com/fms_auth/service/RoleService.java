/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.service;
import net.com.fms_auth.dto.PrivilegesDTO;
import net.com.fms_auth.dto.RoleDTO;
import net.com.fms_auth.dto.common.CommonPageReqDTO;
import net.com.fms_auth.dto.common.GetAllRolesRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
public interface RoleService {
    ResponseEntity addRole(RoleDTO roleDTO);
    ResponseEntity getAllRoles(GetAllRolesRequestDTO getAllRolesRequestDTO);
    ResponseEntity getRollByID(int roleID);
    ResponseEntity deleteRoleByID(int roleID);
    ResponseEntity getPrivilegesByRoleID(int roleID);
    ResponseEntity updateRolePrivileges(PrivilegesDTO privilegesDTO);
    ResponseEntity getAllRoleNames();
    ResponseEntity getAllPageNames();
    ResponseEntity getAllSectionNames();
    ResponseEntity getAllTaskNames();
}
