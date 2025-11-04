/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.dto.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUserFilterDTO {
    private Integer userId;
    private String username;
    private String fullName;
    private String nic;
    private Integer[] userRole;
    private String empId;
    private String email;
    private String contact;
    private String status;
    private Integer page;
    private Integer size;
}
