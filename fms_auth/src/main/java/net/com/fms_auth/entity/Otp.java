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
@Table(name = "otp")
@NamedQueries({
    @NamedQuery(name = "Otp.findAll", query = "SELECT o FROM Otp o")
    , @NamedQuery(name = "Otp.findByOtpId", query = "SELECT o FROM Otp o WHERE o.otpId = :otpId")
    , @NamedQuery(name = "Otp.findByCreatedAt", query = "SELECT o FROM Otp o WHERE o.createdAt = :createdAt")
    , @NamedQuery(name = "Otp.findByCreatedBy", query = "SELECT o FROM Otp o WHERE o.createdBy = :createdBy")
    , @NamedQuery(name = "Otp.findByDeviceType", query = "SELECT o FROM Otp o WHERE o.deviceType = :deviceType")
    , @NamedQuery(name = "Otp.findByExpireAt", query = "SELECT o FROM Otp o WHERE o.expireAt = :expireAt")
    , @NamedQuery(name = "Otp.findByOtp", query = "SELECT o FROM Otp o WHERE o.otp = :otp")
    , @NamedQuery(name = "Otp.findByType", query = "SELECT o FROM Otp o WHERE o.type = :type")
    , @NamedQuery(name = "Otp.findByUpdatedAt", query = "SELECT o FROM Otp o WHERE o.updatedAt = :updatedAt")
    , @NamedQuery(name = "Otp.findByUpdatedBy", query = "SELECT o FROM Otp o WHERE o.updatedBy = :updatedBy")})
public class Otp implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "otp_id")
    private Integer otpId;
    @Basic(optional = false)
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Basic(optional = false)
    @Column(name = "created_by")
    private String createdBy;
    @Basic(optional = false)
    @Column(name = "device_type")
    private String deviceType;
    @Basic(optional = false)
    @Column(name = "expire_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expireAt;
    @Basic(optional = false)
    @Column(name = "otp")
    private int otp;
    @Basic(optional = false)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Basic(optional = false)
    @Column(name = "updated_by")
    private String updatedBy;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User userId;
    public Otp() {
    }
    public Otp(Integer otpId) {
        this.otpId = otpId;
    }
    public Otp(Integer otpId, Date createdAt, String createdBy, String deviceType, Date expireAt, int otp, String type, Date updatedAt, String updatedBy) {
        this.otpId = otpId;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.deviceType = deviceType;
        this.expireAt = expireAt;
        this.otp = otp;
        this.type = type;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }
    public Integer getOtpId() {
        return otpId;
    }
    public void setOtpId(Integer otpId) {
        this.otpId = otpId;
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
    public String getDeviceType() {
        return deviceType;
    }
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
    public Date getExpireAt() {
        return expireAt;
    }
    public void setExpireAt(Date expireAt) {
        this.expireAt = expireAt;
    }
    public int getOtp() {
        return otp;
    }
    public void setOtp(int otp) {
        this.otp = otp;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
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
    public User getUserId() {
        return userId;
    }
    public void setUserId(User userId) {
        this.userId = userId;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otpId != null ? otpId.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Otp)) {
            return false;
        }
        Otp other = (Otp) object;
        if ((this.otpId == null && other.otpId != null) || (this.otpId != null && !this.otpId.equals(other.otpId))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "hibernateexample.Otp[ otpId=" + otpId + " ]";
    }
}
