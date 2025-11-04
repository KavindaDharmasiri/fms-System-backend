/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.entity; 
 import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "fms_rule")
@NamedQueries({
    @NamedQuery(name = "FmsRule.findAll", query = "SELECT e FROM FmsRule e")
    , @NamedQuery(name = "FmsRule.findByfmsRuleId", query = "SELECT e FROM FmsRule e WHERE e.fmsRuleId = :fmsRuleId")
    , @NamedQuery(name = "FmsRule.findByRuleUuid", query = "SELECT e FROM FmsRule e WHERE e.ruleUuid = :ruleUuid")
    , @NamedQuery(name = "FmsRule.findByFromDate", query = "SELECT e FROM FmsRule e WHERE e.fromDate = :fromDate")
    , @NamedQuery(name = "FmsRule.findByToDate", query = "SELECT e FROM FmsRule e WHERE e.toDate = :toDate")
    , @NamedQuery(name = "FmsRule.findByStatus", query = "SELECT e FROM FmsRule e WHERE e.status = :status")
    , @NamedQuery(name = "FmsRule.findByFinalRiskScore", query = "SELECT e FROM FmsRule e WHERE e.finalRiskScore = :finalRiskScore")
    , @NamedQuery(name = "FmsRule.findByCreatedAt", query = "SELECT e FROM FmsRule e WHERE e.createdAt = :createdAt")
    , @NamedQuery(name = "FmsRule.findByUpdatedAt", query = "SELECT e FROM FmsRule e WHERE e.updatedAt = :updatedAt")
    , @NamedQuery(name = "FmsRule.findByCreatedBy", query = "SELECT e FROM FmsRule e WHERE e.createdBy = :createdBy")
    , @NamedQuery(name = "FmsRule.findByUpdatedBy", query = "SELECT e FROM FmsRule e WHERE e.updatedBy = :updatedBy")})
public class FmsRule implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "fms_rule_id")
    private Integer fmsRuleId;
    @Basic(optional = false)
    @Column(name = "rule_uuid")
    private String ruleUuid;
    @Basic(optional = false)
    @Lob
    @Column(name = "rule_name")
    private String ruleName;
    @Lob
    @Column(name = "description")
    private String description;
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
    @Column(name = "final_risk_score")
    private Double finalRiskScore;
    @Basic(optional = false)
    @Lob
    @Column(name = "final_rule")
    private String finalRule;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fmsRuleId")
    private Collection<FmsRuleCondition> fmsRuleConditionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fmsRuleId")
    private Collection<TransactionFlaggedRules> transactionFlaggedRulesCollection;
    @JoinColumn(name = "payment_network_id", referencedColumnName = "payment_network_id")
    @ManyToOne(optional = false)
    private PaymentNetwork paymentNetworkId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fmsRuleId")
    private Collection<RuleGroupRule> ruleGroupRuleCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fmsRuleId")
    private Collection<TestTransactionFlaggedRules> testTransactionFlaggedRulesCollection;
    public FmsRule() {
    }
    public FmsRule(Integer fmsRuleId) {
        this.fmsRuleId = fmsRuleId;
    }
    public FmsRule(Integer fmsRuleId, String ruleUuid, String ruleName, String status, double finalRiskScore, String finalRule, Date createdAt, Date updatedAt) {
        this.fmsRuleId = fmsRuleId;
        this.ruleUuid = ruleUuid;
        this.ruleName = ruleName;
        this.status = status;
        this.finalRiskScore = finalRiskScore;
        this.finalRule = finalRule;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public Integer getFmsRuleId() {
        return fmsRuleId;
    }
    public void setFmsRuleId(Integer fmsRuleId) {
        this.fmsRuleId = fmsRuleId;
    }
    public String getRuleUuid() {
        return ruleUuid;
    }
    public void setRuleUuid(String ruleUuid) {
        this.ruleUuid = ruleUuid;
    }
    public String getRuleName() {
        return ruleName;
    }
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
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
    public double getFinalRiskScore() {
        return finalRiskScore;
    }
    public void setFinalRiskScore(double finalRiskScore) {
        this.finalRiskScore = finalRiskScore;
    }
    public String getFinalRule() {
        return finalRule;
    }
    public void setFinalRule(String finalRule) {
        this.finalRule = finalRule;
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
    public Collection<FmsRuleCondition> getFmsRuleConditionCollection() {
        return fmsRuleConditionCollection;
    }
    public void setFmsRuleConditionCollection(Collection<FmsRuleCondition> fmsRuleConditionCollection) {
        this.fmsRuleConditionCollection = fmsRuleConditionCollection;
    }
    public Collection<TransactionFlaggedRules> getTransactionFlaggedRulesCollection() {
        return transactionFlaggedRulesCollection;
    }
    public void setTransactionFlaggedRulesCollection(Collection<TransactionFlaggedRules> transactionFlaggedRulesCollection) {
        this.transactionFlaggedRulesCollection = transactionFlaggedRulesCollection;
    }
    public PaymentNetwork getPaymentNetworkId() {
        return paymentNetworkId;
    }
    public void setPaymentNetworkId(PaymentNetwork paymentNetworkId) {
        this.paymentNetworkId = paymentNetworkId;
    }
    public Collection<RuleGroupRule> getRuleGroupRuleCollection() {
        return ruleGroupRuleCollection;
    }
    public void setRuleGroupRuleCollection(Collection<RuleGroupRule> ruleGroupRuleCollection) {
        this.ruleGroupRuleCollection = ruleGroupRuleCollection;
    }
    public Collection<TestTransactionFlaggedRules> getTestTransactionFlaggedRulesCollection() {
        return testTransactionFlaggedRulesCollection;
    }
    public void setTestTransactionFlaggedRulesCollection(Collection<TestTransactionFlaggedRules> testTransactionFlaggedRulesCollection) {
        this.testTransactionFlaggedRulesCollection = testTransactionFlaggedRulesCollection;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fmsRuleId != null ? fmsRuleId.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FmsRule)) {
            return false;
        }
        FmsRule other = (FmsRule) object;
        if ((this.fmsRuleId == null && other.fmsRuleId != null) || (this.fmsRuleId != null && !this.fmsRuleId.equals(other.fmsRuleId))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "hibernateexample.FmsRule[ fmsRuleId=" + fmsRuleId + " ]";
    }
}
