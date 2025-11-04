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
@Table(name = "password_reset_request")
@NamedQueries({
    @NamedQuery(name = "PasswordResetRequest.findAll", query = "SELECT p FROM PasswordResetRequest p")
    , @NamedQuery(name = "PasswordResetRequest.findById", query = "SELECT p FROM PasswordResetRequest p WHERE p.id = :id")
    , @NamedQuery(name = "PasswordResetRequest.findByCreatedTime", query = "SELECT p FROM PasswordResetRequest p WHERE p.createdTime = :createdTime")
    , @NamedQuery(name = "PasswordResetRequest.findByLastUpdatedTime", query = "SELECT p FROM PasswordResetRequest p WHERE p.lastUpdatedTime = :lastUpdatedTime")
    , @NamedQuery(name = "PasswordResetRequest.findByRequestKey", query = "SELECT p FROM PasswordResetRequest p WHERE p.requestKey = :requestKey")
    , @NamedQuery(name = "PasswordResetRequest.findByRequestStatus", query = "SELECT p FROM PasswordResetRequest p WHERE p.requestStatus = :requestStatus")
    , @NamedQuery(name = "PasswordResetRequest.findByUsername", query = "SELECT p FROM PasswordResetRequest p WHERE p.username = :username")})
public class PasswordResetRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;
    @Basic(optional = false)
    @Column(name = "last_updated_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedTime;
    @Basic(optional = false)
    @Column(name = "request_key")
    private String requestKey;
    @Basic(optional = false)
    @Column(name = "request_status")
    private String requestStatus;
    @Basic(optional = false)
    @Column(name = "username")
    private String username;
    public PasswordResetRequest() {
    }
    public PasswordResetRequest(Integer id) {
        this.id = id;
    }
    public PasswordResetRequest(Integer id, Date createdTime, Date lastUpdatedTime, String requestKey, String requestStatus, String username) {
        this.id = id;
        this.createdTime = createdTime;
        this.lastUpdatedTime = lastUpdatedTime;
        this.requestKey = requestKey;
        this.requestStatus = requestStatus;
        this.username = username;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Date getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }
    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }
    public String getRequestKey() {
        return requestKey;
    }
    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
    }
    public String getRequestStatus() {
        return requestStatus;
    }
    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PasswordResetRequest)) {
            return false;
        }
        PasswordResetRequest other = (PasswordResetRequest) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "hibernateexample.PasswordResetRequest[ id=" + id + " ]";
    }
}
