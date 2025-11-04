/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.entity; 
 import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "fms_rule_condition")
@NamedQueries({
    @NamedQuery(name = "FmsRuleCondition.findAll", query = "SELECT e FROM FmsRuleCondition e")
    , @NamedQuery(name = "FmsRuleCondition.findByFmsRuleConditionId", query = "SELECT e FROM FmsRuleCondition e WHERE e.FmsRuleConditionId = :FmsRuleConditionId")
    , @NamedQuery(name = "FmsRuleCondition.findByOperator", query = "SELECT e FROM FmsRuleCondition e WHERE e.operator = :operator")
    , @NamedQuery(name = "FmsRuleCondition.findByRiskScore", query = "SELECT e FROM FmsRuleCondition e WHERE e.riskScore = :riskScore")
    , @NamedQuery(name = "FmsRuleCondition.findByCreatedAt", query = "SELECT e FROM FmsRuleCondition e WHERE e.createdAt = :createdAt")
    , @NamedQuery(name = "FmsRuleCondition.findByUpdatedAt", query = "SELECT e FROM FmsRuleCondition e WHERE e.updatedAt = :updatedAt")
    , @NamedQuery(name = "FmsRuleCondition.findByCreatedBy", query = "SELECT e FROM FmsRuleCondition e WHERE e.createdBy = :createdBy")
    , @NamedQuery(name = "FmsRuleCondition.findByUpdatedBy", query = "SELECT e FROM FmsRuleCondition e WHERE e.updatedBy = :updatedBy")})
public class FmsRuleCondition implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "fms_rule_condition_id")
    private Integer FmsRuleConditionId;
    @Basic(optional = false)
    @Column(name = "operator")
    private String operator;
    @Basic(optional = false)
    @Lob
    @Column(name = "value")
    private String value;
    @Basic(optional = false)
    @Column(name = "risk_score")
    private double riskScore;
    @Basic(optional = false)
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Basic(optional = false)
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_by")
    private String updatedBy;
    @JoinColumn(name = "fms_element_id", referencedColumnName = "fms_element_id")
    @ManyToOne(optional = false)
    private FmsElement fmsElementId;
    @JoinColumn(name = "fms_rule_id", referencedColumnName = "fms_rule_id")
    @ManyToOne(optional = false)
    private FmsRule fmsRuleId;
    public FmsRuleCondition() {
    }
    public FmsRuleCondition(Integer FmsRuleConditionId) {
        this.FmsRuleConditionId = FmsRuleConditionId;
    }
    public FmsRuleCondition(Integer FmsRuleConditionId, String operator, String value, double riskScore, Date createdAt, Date updatedAt) {
        this.FmsRuleConditionId = FmsRuleConditionId;
        this.operator = operator;
        this.value = value;
        this.riskScore = riskScore;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public Integer getFmsRuleConditionId() {
        return FmsRuleConditionId;
    }
    public void setFmsRuleConditionId(Integer FmsRuleConditionId) {
        this.FmsRuleConditionId = FmsRuleConditionId;
    }
    public String getOperator() {
        return operator;
    }
    public void setOperator(String operator) {
        this.operator = operator;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public double getRiskScore() {
        return riskScore;
    }
    public void setRiskScore(double riskScore) {
        this.riskScore = riskScore;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public Date getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    public String getUpdatedBy() {
        return updatedBy;
    }
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    public FmsElement getFmsElementId() {
        return fmsElementId;
    }
    public void setFmsElementId(FmsElement fmsElementId) {
        this.fmsElementId = fmsElementId;
    }
    public FmsRule getFmsRuleId() {
        return fmsRuleId;
    }
    public void setFmsRuleId(FmsRule fmsRuleId) {
        this.fmsRuleId = fmsRuleId;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (FmsRuleConditionId != null ? FmsRuleConditionId.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FmsRuleCondition)) {
            return false;
        }
        FmsRuleCondition other = (FmsRuleCondition) object;
        if ((this.FmsRuleConditionId == null && other.FmsRuleConditionId != null) || (this.FmsRuleConditionId != null && !this.FmsRuleConditionId.equals(other.FmsRuleConditionId))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "hibernateexample.FmsRuleCondition[ FmsRuleConditionId=" + FmsRuleConditionId + " ]";
    }
}
