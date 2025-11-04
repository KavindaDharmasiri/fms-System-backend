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
public class FmsRuleConditionDTO {
  private Integer FmsRuleConditionId;
  private String operator;
  private String value;
  private double riskScore;
  private Date createdAt;
  private Date updatedAt;
  private String createdBy;
  private String updatedBy;
  private Integer fmsElementId;
  private String FmsElementOperator;
  private String FmsElementValue;
  private FmsElementDTO FmsElement;
  private Integer fmsRuleId;
  private FmsRuleDTO FmsRule;
}
