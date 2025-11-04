/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleDTO {
    private Integer roleId;
    private String roleName;
    private String roleCode;
    private Date createdAt;
    private Date updatedAt;
    private String status;
    private String createdBy;
    private String updatedBy;
}
