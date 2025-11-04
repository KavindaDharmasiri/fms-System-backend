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
@Table(name = "transaction_flagged_rules")
@NamedQueries({
    @NamedQuery(name = "TransactionFlaggedRules.findAll", query = "SELECT t FROM TransactionFlaggedRules t")
    , @NamedQuery(name = "TransactionFlaggedRules.findByTransactionFlaggedRulesId", query = "SELECT t FROM TransactionFlaggedRules t WHERE t.transactionFlaggedRulesId = :transactionFlaggedRulesId")
    , @NamedQuery(name = "TransactionFlaggedRules.findByCreatedAt", query = "SELECT t FROM TransactionFlaggedRules t WHERE t.createdAt = :createdAt")
    , @NamedQuery(name = "TransactionFlaggedRules.findByUpdatedAt", query = "SELECT t FROM TransactionFlaggedRules t WHERE t.updatedAt = :updatedAt")
    , @NamedQuery(name = "TransactionFlaggedRules.findByStatus", query = "SELECT t FROM TransactionFlaggedRules t WHERE t.status = :status")
    , @NamedQuery(name = "TransactionFlaggedRules.findByRiskScore", query = "SELECT t FROM TransactionFlaggedRules t WHERE t.riskScore = :riskScore")
    , @NamedQuery(name = "TransactionFlaggedRules.findByFlag", query = "SELECT t FROM TransactionFlaggedRules t WHERE t.flag = :flag")
    , @NamedQuery(name = "TransactionFlaggedRules.findByCreatedBy", query = "SELECT t FROM TransactionFlaggedRules t WHERE t.createdBy = :createdBy")
    , @NamedQuery(name = "TransactionFlaggedRules.findByUpdatedBy", query = "SELECT t FROM TransactionFlaggedRules t WHERE t.updatedBy = :updatedBy")})
public class TransactionFlaggedRules implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "transaction_flagged_rules_id")
    private Integer transactionFlaggedRulesId;
    @Basic(optional = false)
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Basic(optional = false)
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Column(name = "status")
    private String status;
    @Column(name = "risk_score")
    private Double riskScore;
    @Column(name = "flag")
    private String flag;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_by")
    private String updatedBy;
    @JoinColumn(name = "fms_rule_id", referencedColumnName = "fms_rule_id")
    @ManyToOne(optional = false)
    private FmsRule fmsRuleId;
    @JoinColumn(name = "rule_group_id", referencedColumnName = "rule_group_id")
    @ManyToOne
    private RuleGroup ruleGroupId;
    @JoinColumn(name = "transaction_history_id", referencedColumnName = "transaction_history_id")
    @ManyToOne(optional = false)
    private TransactionHistory transactionHistoryId;
    public TransactionFlaggedRules() {
    }
    public TransactionFlaggedRules(Integer transactionFlaggedRulesId) {
        this.transactionFlaggedRulesId = transactionFlaggedRulesId;
    }
    public TransactionFlaggedRules(Integer transactionFlaggedRulesId, Date createdAt, Date updatedAt) {
        this.transactionFlaggedRulesId = transactionFlaggedRulesId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public Integer getTransactionFlaggedRulesId() {
        return transactionFlaggedRulesId;
    }
    public void setTransactionFlaggedRulesId(Integer transactionFlaggedRulesId) {
        this.transactionFlaggedRulesId = transactionFlaggedRulesId;
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
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Double getRiskScore() {
        return riskScore;
    }
    public void setRiskScore(Double riskScore) {
        this.riskScore = riskScore;
    }
    public String getFlag() {
        return flag;
    }
    public void setFlag(String flag) {
        this.flag = flag;
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
    public TransactionHistory getTransactionHistoryId() {
        return transactionHistoryId;
    }
    public void setTransactionHistoryId(TransactionHistory transactionHistoryId) {
        this.transactionHistoryId = transactionHistoryId;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transactionFlaggedRulesId != null ? transactionFlaggedRulesId.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TransactionFlaggedRules)) {
            return false;
        }
        TransactionFlaggedRules other = (TransactionFlaggedRules) object;
        if ((this.transactionFlaggedRulesId == null && other.transactionFlaggedRulesId != null) || (this.transactionFlaggedRulesId != null && !this.transactionFlaggedRulesId.equals(other.transactionFlaggedRulesId))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "hibernateexample.TransactionFlaggedRules[ transactionFlaggedRulesId=" + transactionFlaggedRulesId + " ]";
    }
}
