/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePrivilegeDTO {
    private Integer rolePrivilegeId;
    private String status;
    private Date created;
    private Date updated;
    private String createdBy;
    private String updatedBy;
    private Date createdAt;
    private Date updatedAt;
    private Integer privilegeId;
    private PrivilegeDTO privilege;
    private Integer roleId;
    private RoleDTO role;
}
