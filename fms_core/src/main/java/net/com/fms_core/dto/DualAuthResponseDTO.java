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
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DualAuthResponseDTO {
    private String configuration;
    private String task;
    private String lastModifiedUser;
    private String newModifiedUser;
    private List<ParameterDTO> parameterDTOList;
}
