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
@Table(name = "test_transaction_flagged_rules")
@NamedQueries({
    @NamedQuery(name = "TestTransactionFlaggedRules.findAll", query = "SELECT t FROM TestTransactionFlaggedRules t")
    , @NamedQuery(name = "TestTransactionFlaggedRules.findByTestTransactionFlaggedRulesId", query = "SELECT t FROM TestTransactionFlaggedRules t WHERE t.testTransactionFlaggedRulesId = :testTransactionFlaggedRulesId")
    , @NamedQuery(name = "TestTransactionFlaggedRules.findByCreatedAt", query = "SELECT t FROM TestTransactionFlaggedRules t WHERE t.createdAt = :createdAt")
    , @NamedQuery(name = "TestTransactionFlaggedRules.findByUpdatedAt", query = "SELECT t FROM TestTransactionFlaggedRules t WHERE t.updatedAt = :updatedAt")
    , @NamedQuery(name = "TestTransactionFlaggedRules.findByStatus", query = "SELECT t FROM TestTransactionFlaggedRules t WHERE t.status = :status")
    , @NamedQuery(name = "TestTransactionFlaggedRules.findByRiskScore", query = "SELECT t FROM TestTransactionFlaggedRules t WHERE t.riskScore = :riskScore")
    , @NamedQuery(name = "TestTransactionFlaggedRules.findByFlag", query = "SELECT t FROM TestTransactionFlaggedRules t WHERE t.flag = :flag")
    , @NamedQuery(name = "TestTransactionFlaggedRules.findByCreatedBy", query = "SELECT t FROM TestTransactionFlaggedRules t WHERE t.createdBy = :createdBy")
    , @NamedQuery(name = "TestTransactionFlaggedRules.findByUpdatedBy", query = "SELECT t FROM TestTransactionFlaggedRules t WHERE t.updatedBy = :updatedBy")})
public class TestTransactionFlaggedRules implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "test_transaction_flagged_rules_id")
    private Integer testTransactionFlaggedRulesId;
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
    @JoinColumn(name = "test_transaction_id", referencedColumnName = "test_transaction_id")
    @ManyToOne(optional = false)
    private TestTransaction testTransactionId;
    public TestTransactionFlaggedRules() {
    }
    public TestTransactionFlaggedRules(Integer testTransactionFlaggedRulesId) {
        this.testTransactionFlaggedRulesId = testTransactionFlaggedRulesId;
    }
    public TestTransactionFlaggedRules(Integer testTransactionFlaggedRulesId, Date createdAt, Date updatedAt) {
        this.testTransactionFlaggedRulesId = testTransactionFlaggedRulesId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public Integer getTestTransactionFlaggedRulesId() {
        return testTransactionFlaggedRulesId;
    }
    public void setTestTransactionFlaggedRulesId(Integer testTransactionFlaggedRulesId) {
        this.testTransactionFlaggedRulesId = testTransactionFlaggedRulesId;
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
    public TestTransaction getTestTransactionId() {
        return testTransactionId;
    }
    public void setTestTransactionId(TestTransaction testTransactionId) {
        this.testTransactionId = testTransactionId;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (testTransactionFlaggedRulesId != null ? testTransactionFlaggedRulesId.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TestTransactionFlaggedRules)) {
            return false;
        }
        TestTransactionFlaggedRules other = (TestTransactionFlaggedRules) object;
        if ((this.testTransactionFlaggedRulesId == null && other.testTransactionFlaggedRulesId != null) || (this.testTransactionFlaggedRulesId != null && !this.testTransactionFlaggedRulesId.equals(other.testTransactionFlaggedRulesId))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "hibernateexample.TestTransactionFlaggedRules[ testTransactionFlaggedRulesId=" + testTransactionFlaggedRulesId + " ]";
    }
}
