/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.dto.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUserResponseDTO {
    private Integer userID;
    private String userCategory;
    private String nic;
    private String contact;
    private int[] role;
    private String email;
    private String employeeID;
    private String fullName;
    private String city;
    private String address;
    private Boolean status;
}
