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
@Table(name = "rule_group_role")
@NamedQueries({
    @NamedQuery(name = "RuleGroupRole.findAll", query = "SELECT r FROM RuleGroupRole r")
    , @NamedQuery(name = "RuleGroupRole.findByRuleGroupRoleId", query = "SELECT r FROM RuleGroupRole r WHERE r.ruleGroupRoleId = :ruleGroupRoleId")
    , @NamedQuery(name = "RuleGroupRole.findByStatus", query = "SELECT r FROM RuleGroupRole r WHERE r.status = :status")
    , @NamedQuery(name = "RuleGroupRole.findByCreatedAt", query = "SELECT r FROM RuleGroupRole r WHERE r.createdAt = :createdAt")
    , @NamedQuery(name = "RuleGroupRole.findByUpdatedAt", query = "SELECT r FROM RuleGroupRole r WHERE r.updatedAt = :updatedAt")
    , @NamedQuery(name = "RuleGroupRole.findByCreatedBy", query = "SELECT r FROM RuleGroupRole r WHERE r.createdBy = :createdBy")
    , @NamedQuery(name = "RuleGroupRole.findByUpdatedBy", query = "SELECT r FROM RuleGroupRole r WHERE r.updatedBy = :updatedBy")})
public class RuleGroupRole implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rule_group_role_id")
    private Integer ruleGroupRoleId;
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
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    @ManyToOne(optional = false)
    private Role roleId;
    @JoinColumn(name = "rule_group_id", referencedColumnName = "rule_group_id")
    @ManyToOne(optional = false)
    private RuleGroup ruleGroupId;
    public RuleGroupRole() {
    }
    public RuleGroupRole(Integer ruleGroupRoleId) {
        this.ruleGroupRoleId = ruleGroupRoleId;
    }
    public RuleGroupRole(Integer ruleGroupRoleId, String status, Date createdAt, Date updatedAt) {
        this.ruleGroupRoleId = ruleGroupRoleId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public Integer getRuleGroupRoleId() {
        return ruleGroupRoleId;
    }
    public void setRuleGroupRoleId(Integer ruleGroupRoleId) {
        this.ruleGroupRoleId = ruleGroupRoleId;
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
    public Role getRoleId() {
        return roleId;
    }
    public void setRoleId(Role roleId) {
        this.roleId = roleId;
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
        hash += (ruleGroupRoleId != null ? ruleGroupRoleId.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RuleGroupRole)) {
            return false;
        }
        RuleGroupRole other = (RuleGroupRole) object;
        if ((this.ruleGroupRoleId == null && other.ruleGroupRoleId != null) || (this.ruleGroupRoleId != null && !this.ruleGroupRoleId.equals(other.ruleGroupRoleId))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "hibernateexample.RuleGroupRole[ ruleGroupRoleId=" + ruleGroupRoleId + " ]";
    }
}
