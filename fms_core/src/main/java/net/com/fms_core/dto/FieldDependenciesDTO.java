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
public class FieldDependenciesDTO {
    private Integer fieldDependenciesId;
    private String mainOperator;
    private String value;
    private String depOperator;
    private String depValue;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    private Integer depElementId;
    private FmsElementDTO depElement;
    private Integer fmsElementId;
    private FmsElementDTO FmsElement;
}
