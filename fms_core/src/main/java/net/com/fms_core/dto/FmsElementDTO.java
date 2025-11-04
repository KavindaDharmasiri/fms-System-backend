/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.dto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.com.fms_core.entity.FmsRuleCondition;
import net.com.fms_core.entity.FieldDependencies;
import net.com.fms_core.entity.PaymentNetwork;
import java.util.Collection;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FmsElementDTO {
    private Integer fmsElementId;
    private Integer paymentNetworkId;
    private String networkName;
    private String elementCode;
    private String elementName;
    private String variableName;
    private String validation;
    private String validationMessage;
    private double riskWeight;
    private String description;
    private String operator;
    private String value;
    private String status;
    private Date created;
    private Date updated;
    private String createdBy;
    private String updatedBy;
    private Collection<FmsRuleCondition> fmsRuleConditionCollection;
    private Collection<FieldDependenciesDTO> fieldDependenciesCollection;
    private Collection<FieldDependenciesDTO> fieldDependenciesCollection1;
}
