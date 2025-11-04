/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FilterDualAuthentication {
    private Date from;
    private Date to;
    private String configuration;
    private String task;
    private String modifiedUser;
    private int pageNo;
    private int pageSize;
}
