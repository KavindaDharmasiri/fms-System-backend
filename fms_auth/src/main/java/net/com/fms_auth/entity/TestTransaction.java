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
@Table(name = "test_transaction")
@NamedQueries({
    @NamedQuery(name = "TestTransaction.findAll", query = "SELECT t FROM TestTransaction t")
    , @NamedQuery(name = "TestTransaction.findByTestTransactionId", query = "SELECT t FROM TestTransaction t WHERE t.testTransactionId = :testTransactionId")
    , @NamedQuery(name = "TestTransaction.findByTranUuid", query = "SELECT t FROM TestTransaction t WHERE t.tranUuid = :tranUuid")
    , @NamedQuery(name = "TestTransaction.findByStatus", query = "SELECT t FROM TestTransaction t WHERE t.status = :status")
    , @NamedQuery(name = "TestTransaction.findByCreatedAt", query = "SELECT t FROM TestTransaction t WHERE t.createdAt = :createdAt")
    , @NamedQuery(name = "TestTransaction.findByUpdatedAt", query = "SELECT t FROM TestTransaction t WHERE t.updatedAt = :updatedAt")
    , @NamedQuery(name = "TestTransaction.findByCreatedBy", query = "SELECT t FROM TestTransaction t WHERE t.createdBy = :createdBy")
    , @NamedQuery(name = "TestTransaction.findByUpdatedBy", query = "SELECT t FROM TestTransaction t WHERE t.updatedBy = :updatedBy")})
public class TestTransaction implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "test_transaction_id")
    private Integer testTransactionId;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "testTransactionId")
    private Collection<TestTransactionFlaggedRules> testTransactionFlaggedRulesCollection;
    public TestTransaction() {
    }
    public TestTransaction(Integer testTransactionId) {
        this.testTransactionId = testTransactionId;
    }
    public TestTransaction(Integer testTransactionId, String tranUuid, String tranPacket, String status, Date createdAt, Date updatedAt) {
        this.testTransactionId = testTransactionId;
        this.tranUuid = tranUuid;
        this.tranPacket = tranPacket;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public Integer getTestTransactionId() {
        return testTransactionId;
    }
    public void setTestTransactionId(Integer testTransactionId) {
        this.testTransactionId = testTransactionId;
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
    public void setTestTransactionFlaggedRulesCollection(Collection<TestTransactionFlaggedRules> testTransactionFlaggedRulesCollection) {
        this.testTransactionFlaggedRulesCollection = testTransactionFlaggedRulesCollection;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (testTransactionId != null ? testTransactionId.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TestTransaction)) {
            return false;
        }
        TestTransaction other = (TestTransaction) object;
        if ((this.testTransactionId == null && other.testTransactionId != null) || (this.testTransactionId != null && !this.testTransactionId.equals(other.testTransactionId))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "hibernateexample.TestTransaction[ testTransactionId=" + testTransactionId + " ]";
    }
}
