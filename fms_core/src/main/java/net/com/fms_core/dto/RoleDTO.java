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
import java.util.Collection;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private Integer roleId;
    private String roleCode;
    private String roleName;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    private Collection<UserRoleDTO> userRoleCollection;
    private Collection<RolePrivilegeDTO> rolePrivilegeCollection;
    private Collection<RuleGroupRoleDTO> ruleGroupRoleCollection;
    private Collection<ReactionTemplateRoleDTO> reactionTemplateRoleCollection;
}
