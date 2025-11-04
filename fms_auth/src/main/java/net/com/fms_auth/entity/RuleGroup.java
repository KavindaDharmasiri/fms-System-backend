/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.entity;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "rule_group")
@NamedQueries({
    @NamedQuery(name = "RuleGroup.findAll", query = "SELECT r FROM RuleGroup r")
    , @NamedQuery(name = "RuleGroup.findByRuleGroupId", query = "SELECT r FROM RuleGroup r WHERE r.ruleGroupId = :ruleGroupId")
    , @NamedQuery(name = "RuleGroup.findByRuleGroupUuid", query = "SELECT r FROM RuleGroup r WHERE r.ruleGroupUuid = :ruleGroupUuid")
    , @NamedQuery(name = "RuleGroup.findByGroupName", query = "SELECT r FROM RuleGroup r WHERE r.groupName = :groupName")
    , @NamedQuery(name = "RuleGroup.findByVerdict", query = "SELECT r FROM RuleGroup r WHERE r.verdict = :verdict")
    , @NamedQuery(name = "RuleGroup.findByFromDate", query = "SELECT r FROM RuleGroup r WHERE r.fromDate = :fromDate")
    , @NamedQuery(name = "RuleGroup.findByToDate", query = "SELECT r FROM RuleGroup r WHERE r.toDate = :toDate")
    , @NamedQuery(name = "RuleGroup.findByStatus", query = "SELECT r FROM RuleGroup r WHERE r.status = :status")
    , @NamedQuery(name = "RuleGroup.findByCreatedAt", query = "SELECT r FROM RuleGroup r WHERE r.createdAt = :createdAt")
    , @NamedQuery(name = "RuleGroup.findByUpdatedAt", query = "SELECT r FROM RuleGroup r WHERE r.updatedAt = :updatedAt")
    , @NamedQuery(name = "RuleGroup.findByCreatedBy", query = "SELECT r FROM RuleGroup r WHERE r.createdBy = :createdBy")
    , @NamedQuery(name = "RuleGroup.findByUpdatedBy", query = "SELECT r FROM RuleGroup r WHERE r.updatedBy = :updatedBy")})
public class RuleGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rule_group_id")
    private Integer ruleGroupId;
    @Basic(optional = false)
    @Column(name = "rule_group_uuid")
    private String ruleGroupUuid;
    @Basic(optional = false)
    @Column(name = "group_name")
    private String groupName;
    @Column(name = "verdict")
    private String verdict;
    @Column(name = "from_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fromDate;
    @Column(name = "to_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date toDate;
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
    @OneToMany(mappedBy = "ruleGroupId")
    private Collection<TransactionFlaggedRules> transactionFlaggedRulesCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ruleGroupId")
    private Collection<RuleGroupRule> ruleGroupRuleCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ruleGroupId")
    private Collection<RuleGroupRole> ruleGroupRoleCollection;
    @OneToMany(mappedBy = "ruleGroupId")
    private Collection<TestTransactionFlaggedRules> testTransactionFlaggedRulesCollection;
    @JoinColumn(name = "payment_network_id", referencedColumnName = "payment_network_id")
    @ManyToOne(optional = false)
    private PaymentNetwork paymentNetworkId;
    @JoinColumn(name = "reaction_template_id", referencedColumnName = "reaction_template_id")
    @ManyToOne(optional = false)
    private ReactionTemplate reactionTemplateId;
    public RuleGroup() {
    }
    public RuleGroup(Integer ruleGroupId) {
        this.ruleGroupId = ruleGroupId;
    }
    public RuleGroup(Integer ruleGroupId, String ruleGroupUuid, String groupName, String status, Date createdAt, Date updatedAt) {
        this.ruleGroupId = ruleGroupId;
        this.ruleGroupUuid = ruleGroupUuid;
        this.groupName = groupName;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public Integer getRuleGroupId() {
        return ruleGroupId;
    }
    public void setRuleGroupId(Integer ruleGroupId) {
        this.ruleGroupId = ruleGroupId;
    }
    public String getRuleGroupUuid() {
        return ruleGroupUuid;
    }
    public void setRuleGroupUuid(String ruleGroupUuid) {
        this.ruleGroupUuid = ruleGroupUuid;
    }
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public String getVerdict() {
        return verdict;
    }
    public void setVerdict(String verdict) {
        this.verdict = verdict;
    }
    public Date getFromDate() {
        return fromDate;
    }
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }
    public Date getToDate() {
        return toDate;
    }
    public void setToDate(Date toDate) {
        this.toDate = toDate;
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
    public void setTransactionFlaggedRulesCollection(Collection<TransactionFlaggedRules> transactionFlaggedRulesCollection) {
        this.transactionFlaggedRulesCollection = transactionFlaggedRulesCollection;
    }
    public void setRuleGroupRuleCollection(Collection<RuleGroupRule> ruleGroupRuleCollection) {
        this.ruleGroupRuleCollection = ruleGroupRuleCollection;
    }
    public void setRuleGroupRoleCollection(Collection<RuleGroupRole> ruleGroupRoleCollection) {
        this.ruleGroupRoleCollection = ruleGroupRoleCollection;
    }
    public void setTestTransactionFlaggedRulesCollection(Collection<TestTransactionFlaggedRules> testTransactionFlaggedRulesCollection) {
        this.testTransactionFlaggedRulesCollection = testTransactionFlaggedRulesCollection;
    }
    public PaymentNetwork getPaymentNetworkId() {
        return paymentNetworkId;
    }
    public void setPaymentNetworkId(PaymentNetwork paymentNetworkId) {
        this.paymentNetworkId = paymentNetworkId;
    }
    public ReactionTemplate getReactionTemplateId() {
        return reactionTemplateId;
    }
    public void setReactionTemplateId(ReactionTemplate reactionTemplateId) {
        this.reactionTemplateId = reactionTemplateId;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ruleGroupId != null ? ruleGroupId.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RuleGroup)) {
            return false;
        }
        RuleGroup other = (RuleGroup) object;
        if ((this.ruleGroupId == null && other.ruleGroupId != null) || (this.ruleGroupId != null && !this.ruleGroupId.equals(other.ruleGroupId))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "hibernateexample.RuleGroup[ ruleGroupId=" + ruleGroupId + " ]";
    }
}
