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
@Table(name = "otp_configuration")
@NamedQueries({
    @NamedQuery(name = "OtpConfiguration.findAll", query = "SELECT o FROM OtpConfiguration o")
    , @NamedQuery(name = "OtpConfiguration.findByOtpConfigId", query = "SELECT o FROM OtpConfiguration o WHERE o.otpConfigId = :otpConfigId")
    , @NamedQuery(name = "OtpConfiguration.findByCreatedAt", query = "SELECT o FROM OtpConfiguration o WHERE o.createdAt = :createdAt")
    , @NamedQuery(name = "OtpConfiguration.findByCreatedBy", query = "SELECT o FROM OtpConfiguration o WHERE o.createdBy = :createdBy")
    , @NamedQuery(name = "OtpConfiguration.findByDigits", query = "SELECT o FROM OtpConfiguration o WHERE o.digits = :digits")
    , @NamedQuery(name = "OtpConfiguration.findByExpirationTimeInSeconds", query = "SELECT o FROM OtpConfiguration o WHERE o.expirationTimeInSeconds = :expirationTimeInSeconds")
    , @NamedQuery(name = "OtpConfiguration.findBySecretKey", query = "SELECT o FROM OtpConfiguration o WHERE o.secretKey = :secretKey")
    , @NamedQuery(name = "OtpConfiguration.findByStatus", query = "SELECT o FROM OtpConfiguration o WHERE o.status = :status")
    , @NamedQuery(name = "OtpConfiguration.findByUpdatedAt", query = "SELECT o FROM OtpConfiguration o WHERE o.updatedAt = :updatedAt")
    , @NamedQuery(name = "OtpConfiguration.findByUpdatedBy", query = "SELECT o FROM OtpConfiguration o WHERE o.updatedBy = :updatedBy")
    , @NamedQuery(name = "OtpConfiguration.findByVersion", query = "SELECT o FROM OtpConfiguration o WHERE o.version = :version")})
public class OtpConfiguration implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "otp_config_id")
    private Integer otpConfigId;
    @Basic(optional = false)
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Basic(optional = false)
    @Column(name = "created_by")
    private String createdBy;
    @Basic(optional = false)
    @Column(name = "digits")
    private int digits;
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
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Basic(optional = false)
    @Column(name = "updated_by")
    private String updatedBy;
    @Basic(optional = false)
    @Column(name = "version")
    private String version;
    public OtpConfiguration() {
    }
    public OtpConfiguration(Integer otpConfigId) {
        this.otpConfigId = otpConfigId;
    }
    public OtpConfiguration(Integer otpConfigId, Date createdAt, String createdBy, int digits, int expirationTimeInSeconds, String secretKey, String status, Date updatedAt, String updatedBy, String version) {
        this.otpConfigId = otpConfigId;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.digits = digits;
        this.expirationTimeInSeconds = expirationTimeInSeconds;
        this.secretKey = secretKey;
        this.status = status;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.version = version;
    }
    public Integer getOtpConfigId() {
        return otpConfigId;
    }
    public void setOtpConfigId(Integer otpConfigId) {
        this.otpConfigId = otpConfigId;
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
    public int getDigits() {
        return digits;
    }
    public void setDigits(int digits) {
        this.digits = digits;
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
        hash += (otpConfigId != null ? otpConfigId.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OtpConfiguration)) {
            return false;
        }
        OtpConfiguration other = (OtpConfiguration) object;
        if ((this.otpConfigId == null && other.otpConfigId != null) || (this.otpConfigId != null && !this.otpConfigId.equals(other.otpConfigId))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "hibernateexample.OtpConfiguration[ otpConfigId=" + otpConfigId + " ]";
    }
}
