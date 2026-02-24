/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.entity; 
 import jakarta.persistence.*;
 import lombok.Builder;
 import org.hibernate.annotations.CreationTimestamp;
 import org.hibernate.annotations.UpdateTimestamp;
 import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "transaction_history")
@NamedQueries({
    @NamedQuery(name = "TransactionHistory.findAll", query = "SELECT t FROM TransactionHistory t")
    , @NamedQuery(name = "TransactionHistory.findByTransactionHistoryId", query = "SELECT t FROM TransactionHistory t WHERE t.transactionHistoryId = :transactionHistoryId")
    , @NamedQuery(name = "TransactionHistory.findByTranUuid", query = "SELECT t FROM TransactionHistory t WHERE t.tranUuid = :tranUuid")
    , @NamedQuery(name = "TransactionHistory.findByStatus", query = "SELECT t FROM TransactionHistory t WHERE t.status = :status")
    , @NamedQuery(name = "TransactionHistory.findByCreatedAt", query = "SELECT t FROM TransactionHistory t WHERE t.createdAt = :createdAt")
    , @NamedQuery(name = "TransactionHistory.findByUpdatedAt", query = "SELECT t FROM TransactionHistory t WHERE t.updatedAt = :updatedAt")
    , @NamedQuery(name = "TransactionHistory.findByCreatedBy", query = "SELECT t FROM TransactionHistory t WHERE t.createdBy = :createdBy")
    , @NamedQuery(name = "TransactionHistory.findByUpdatedBy", query = "SELECT t FROM TransactionHistory t WHERE t.updatedBy = :updatedBy")})
public class TransactionHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "transaction_history_id")
    private Integer transactionHistoryId;
    @Basic(optional = false)
    @Column(name = "tran_uuid")
    private String tranUuid;
    @Basic(optional = false)
    @Lob
    @Column(name = "tran_packet")
    private String tranPacket;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @Column(name = "fraud_percentage")
    private Double fraudPercentage;
    @Column(name = "block_reason")
    private String blockReason;
    @Column(name = "action_status")
    private String actionStatus;
    @Column(name = "reaction_template_name")
    private String reactionTemplateName;
    @Column(name = "sms_enabled")
    private String smsEnabled;
    @Column(name = "email_enabled")
    private String emailEnabled;
    @Column(name = "frm_enabled")
    private String frmEnabled;
    @Column(name = "manual_review_status")
    private String manualReviewStatus;
    @Lob
    @Column(name = "manual_review_reason")
    private String manualReviewReason;
    @Column(name = "reviewed_by")
    private String reviewedBy;
    @Column(name = "reviewed_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reviewedAt;
    @Basic(optional = false)
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;
    @Basic(optional = false)
    @Column(name = "updated_at")
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_by")
    private String updatedBy;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transactionHistoryId")
    private Collection<TransactionFlaggedRules> transactionFlaggedRulesCollection;
    public TransactionHistory() {
    }
    public TransactionHistory(Integer transactionHistoryId) {
        this.transactionHistoryId = transactionHistoryId;
    }
    public TransactionHistory(Integer transactionHistoryId, String tranUuid, String tranPacket, String status, Date createdAt, Date updatedAt) {
        this.transactionHistoryId = transactionHistoryId;
        this.tranUuid = tranUuid;
        this.tranPacket = tranPacket;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public Integer getTransactionHistoryId() {
        return transactionHistoryId;
    }
    public void setTransactionHistoryId(Integer transactionHistoryId) {
        this.transactionHistoryId = transactionHistoryId;
    }
    public String getTranUuid() {
        return tranUuid;
    }
    public void setTranUuid(String tranUuid) {
        this.tranUuid = tranUuid;
    }
    public String getTranPacket() {
        return tranPacket;
    }
    public void setTranPacket(String tranPacket) {
        this.tranPacket = tranPacket;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Double getFraudPercentage() {
        return fraudPercentage;
    }
    public void setFraudPercentage(Double fraudPercentage) {
        this.fraudPercentage = fraudPercentage;
    }
    public String getBlockReason() {
        return blockReason;
    }
    public void setBlockReason(String blockReason) {
        this.blockReason = blockReason;
    }
    public String getActionStatus() {
        return actionStatus;
    }
    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }
    public String getReactionTemplateName() {
        return reactionTemplateName;
    }
    public void setReactionTemplateName(String reactionTemplateName) {
        this.reactionTemplateName = reactionTemplateName;
    }
    public String getSmsEnabled() {
        return smsEnabled;
    }
    public void setSmsEnabled(String smsEnabled) {
        this.smsEnabled = smsEnabled;
    }
    public String getEmailEnabled() {
        return emailEnabled;
    }
    public void setEmailEnabled(String emailEnabled) {
        this.emailEnabled = emailEnabled;
    }
    public String getFrmEnabled() {
        return frmEnabled;
    }
    public void setFrmEnabled(String frmEnabled) {
        this.frmEnabled = frmEnabled;
    }
    public String getManualReviewStatus() {
        return manualReviewStatus;
    }
    public void setManualReviewStatus(String manualReviewStatus) {
        this.manualReviewStatus = manualReviewStatus;
    }
    public String getManualReviewReason() {
        return manualReviewReason;
    }
    public void setManualReviewReason(String manualReviewReason) {
        this.manualReviewReason = manualReviewReason;
    }
    public String getReviewedBy() {
        return reviewedBy;
    }
    public void setReviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
    }
    public Date getReviewedAt() {
        return reviewedAt;
    }
    public void setReviewedAt(Date reviewedAt) {
        this.reviewedAt = reviewedAt;
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
    public Collection<TransactionFlaggedRules> getTransactionFlaggedRulesCollection() {
        return transactionFlaggedRulesCollection;
    }
    public void setTransactionFlaggedRulesCollection(Collection<TransactionFlaggedRules> transactionFlaggedRulesCollection) {
        this.transactionFlaggedRulesCollection = transactionFlaggedRulesCollection;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transactionHistoryId != null ? transactionHistoryId.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TransactionHistory)) {
            return false;
        }
        TransactionHistory other = (TransactionHistory) object;
        if ((this.transactionHistoryId == null && other.transactionHistoryId != null) || (this.transactionHistoryId != null && !this.transactionHistoryId.equals(other.transactionHistoryId))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "hibernateexample.TransactionHistory[ transactionHistoryId=" + transactionHistoryId + " ]";
    }
}
