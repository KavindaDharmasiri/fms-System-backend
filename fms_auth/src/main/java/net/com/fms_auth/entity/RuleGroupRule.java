/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.entity;
import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "rule_group_rule")
@NamedQueries({
    @NamedQuery(name = "RuleGroupRule.findAll", query = "SELECT r FROM RuleGroupRule r")
    , @NamedQuery(name = "RuleGroupRule.findByRuleGroupRuleId", query = "SELECT r FROM RuleGroupRule r WHERE r.ruleGroupRuleId = :ruleGroupRuleId")
    , @NamedQuery(name = "RuleGroupRule.findByStatus", query = "SELECT r FROM RuleGroupRule r WHERE r.status = :status")
    , @NamedQuery(name = "RuleGroupRule.findByCreatedAt", query = "SELECT r FROM RuleGroupRule r WHERE r.createdAt = :createdAt")
    , @NamedQuery(name = "RuleGroupRule.findByUpdatedAt", query = "SELECT r FROM RuleGroupRule r WHERE r.updatedAt = :updatedAt")
    , @NamedQuery(name = "RuleGroupRule.findByCreatedBy", query = "SELECT r FROM RuleGroupRule r WHERE r.createdBy = :createdBy")
    , @NamedQuery(name = "RuleGroupRule.findByUpdatedBy", query = "SELECT r FROM RuleGroupRule r WHERE r.updatedBy = :updatedBy")})
public class RuleGroupRule implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rule_group_rule_id")
    private Integer ruleGroupRuleId;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
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
    @JoinColumn(name = "fms_rule_id", referencedColumnName = "fms_rule_id")
    @ManyToOne(optional = false)
    private FmsRule fmsRuleId;
    @JoinColumn(name = "rule_group_id", referencedColumnName = "rule_group_id")
    @ManyToOne(optional = false)
    private RuleGroup ruleGroupId;
    public RuleGroupRule() {
    }
    public RuleGroupRule(Integer ruleGroupRuleId) {
        this.ruleGroupRuleId = ruleGroupRuleId;
    }
    public RuleGroupRule(Integer ruleGroupRuleId, String status, Date createdAt, Date updatedAt) {
        this.ruleGroupRuleId = ruleGroupRuleId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public Integer getRuleGroupRuleId() {
        return ruleGroupRuleId;
    }
    public void setRuleGroupRuleId(Integer ruleGroupRuleId) {
        this.ruleGroupRuleId = ruleGroupRuleId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
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
    public FmsRule getFmsRuleId() {
        return fmsRuleId;
    }
    public void setFmsRuleId(FmsRule fmsRuleId) {
        this.fmsRuleId = fmsRuleId;
    }
    public RuleGroup getRuleGroupId() {
        return ruleGroupId;
    }
    public void setRuleGroupId(RuleGroup ruleGroupId) {
        this.ruleGroupId = ruleGroupId;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ruleGroupRuleId != null ? ruleGroupRuleId.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RuleGroupRule)) {
            return false;
        }
        RuleGroupRule other = (RuleGroupRule) object;
        if ((this.ruleGroupRuleId == null && other.ruleGroupRuleId != null) || (this.ruleGroupRuleId != null && !this.ruleGroupRuleId.equals(other.ruleGroupRuleId))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "hibernateexample.RuleGroupRule[ ruleGroupRuleId=" + ruleGroupRuleId + " ]";
    }
}
