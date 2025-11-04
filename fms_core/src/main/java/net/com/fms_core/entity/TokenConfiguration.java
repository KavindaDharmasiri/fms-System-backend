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
@Table(name = "token_configuration")
@NamedQueries({
    @NamedQuery(name = "TokenConfiguration.findAll", query = "SELECT t FROM TokenConfiguration t")
    , @NamedQuery(name = "TokenConfiguration.findByTokenConfigId", query = "SELECT t FROM TokenConfiguration t WHERE t.tokenConfigId = :tokenConfigId")
    , @NamedQuery(name = "TokenConfiguration.findByCreatedAt", query = "SELECT t FROM TokenConfiguration t WHERE t.createdAt = :createdAt")
    , @NamedQuery(name = "TokenConfiguration.findByCreatedBy", query = "SELECT t FROM TokenConfiguration t WHERE t.createdBy = :createdBy")
    , @NamedQuery(name = "TokenConfiguration.findByExpirationTimeInSeconds", query = "SELECT t FROM TokenConfiguration t WHERE t.expirationTimeInSeconds = :expirationTimeInSeconds")
    , @NamedQuery(name = "TokenConfiguration.findBySecretKey", query = "SELECT t FROM TokenConfiguration t WHERE t.secretKey = :secretKey")
    , @NamedQuery(name = "TokenConfiguration.findByStatus", query = "SELECT t FROM TokenConfiguration t WHERE t.status = :status")
    , @NamedQuery(name = "TokenConfiguration.findByTokenType", query = "SELECT t FROM TokenConfiguration t WHERE t.tokenType = :tokenType")
    , @NamedQuery(name = "TokenConfiguration.findByUpdatedAt", query = "SELECT t FROM TokenConfiguration t WHERE t.updatedAt = :updatedAt")
    , @NamedQuery(name = "TokenConfiguration.findByUpdatedBy", query = "SELECT t FROM TokenConfiguration t WHERE t.updatedBy = :updatedBy")
    , @NamedQuery(name = "TokenConfiguration.findByVersion", query = "SELECT t FROM TokenConfiguration t WHERE t.version = :version")})
public class TokenConfiguration implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "token_config_id")
    private Integer tokenConfigId;
    @Basic(optional = false)
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Basic(optional = false)
    @Column(name = "created_by")
    private String createdBy;
    @Basic(optional = false)
    @Column(name = "expiration_time_in_seconds")
    private int expirationTimeInSeconds;
    @Basic(optional = false)
    @Column(name = "secret_key")
    private String secretKey;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @Column(name = "token_type")
    private String tokenType;
    @Basic(optional = false)
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Basic(optional = false)
    @Column(name = "updated_by")
    private String updatedBy;
    @Basic(optional = false)
    @Column(name = "version")
    private String version;
    public TokenConfiguration() {
    }
    public TokenConfiguration(Integer tokenConfigId) {
        this.tokenConfigId = tokenConfigId;
    }
    public TokenConfiguration(Integer tokenConfigId, Date createdAt, String createdBy, int expirationTimeInSeconds, String secretKey, String status, String tokenType, Date updatedAt, String updatedBy, String version) {
        this.tokenConfigId = tokenConfigId;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.expirationTimeInSeconds = expirationTimeInSeconds;
        this.secretKey = secretKey;
        this.status = status;
        this.tokenType = tokenType;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.version = version;
    }
    public Integer getTokenConfigId() {
        return tokenConfigId;
    }
    public void setTokenConfigId(Integer tokenConfigId) {
        this.tokenConfigId = tokenConfigId;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    public int getExpirationTimeInSeconds() {
        return expirationTimeInSeconds;
    }
    public void setExpirationTimeInSeconds(int expirationTimeInSeconds) {
        this.expirationTimeInSeconds = expirationTimeInSeconds;
    }
    public String getSecretKey() {
        return secretKey;
    }
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getTokenType() {
        return tokenType;
    }
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
    public Date getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    public String getUpdatedBy() {
        return updatedBy;
    }
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tokenConfigId != null ? tokenConfigId.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TokenConfiguration)) {
            return false;
        }
        TokenConfiguration other = (TokenConfiguration) object;
        if ((this.tokenConfigId == null && other.tokenConfigId != null) || (this.tokenConfigId != null && !this.tokenConfigId.equals(other.tokenConfigId))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "hibernateexample.TokenConfiguration[ tokenConfigId=" + tokenConfigId + " ]";
    }
}
