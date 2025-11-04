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

@Entity
@Table(name = "reaction_template")
@NamedQueries({
    @NamedQuery(name = "ReactionTemplate.findAll", query = "SELECT r FROM ReactionTemplate r")
    , @NamedQuery(name = "ReactionTemplate.findByReactionTemplateId", query = "SELECT r FROM ReactionTemplate r WHERE r.reactionTemplateId = :reactionTemplateId")
    , @NamedQuery(name = "ReactionTemplate.findByTemplateUuid", query = "SELECT r FROM ReactionTemplate r WHERE r.templateUuid = :templateUuid")
    , @NamedQuery(name = "ReactionTemplate.findByTemplateName", query = "SELECT r FROM ReactionTemplate r WHERE r.templateName = :templateName")
    , @NamedQuery(name = "ReactionTemplate.findByStatus", query = "SELECT r FROM ReactionTemplate r WHERE r.status = :status")
    , @NamedQuery(name = "ReactionTemplate.findBySmsEnabled", query = "SELECT r FROM ReactionTemplate r WHERE r.smsEnabled = :smsEnabled")
    , @NamedQuery(name = "ReactionTemplate.findByEmailEnabled", query = "SELECT r FROM ReactionTemplate r WHERE r.emailEnabled = :emailEnabled")
    , @NamedQuery(name = "ReactionTemplate.findByIncludedFlaggedRules", query = "SELECT r FROM ReactionTemplate r WHERE r.includedFlaggedRules = :includedFlaggedRules")
    , @NamedQuery(name = "ReactionTemplate.findByFrmEnabled", query = "SELECT r FROM ReactionTemplate r WHERE r.frmEnabled = :frmEnabled")})
public class ReactionTemplate implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "reaction_template_id")
    private Integer reactionTemplateId;
    @Basic(optional = false)
    @Column(name = "template_uuid")
    private String templateUuid;
    @Basic(optional = false)
    @Column(name = "template_name")
    private String templateName;
    @Lob
    @Column(name = "subject")
    private String subject;
    @Column(name = "status")
    private String status;
    @Column(name = "sms_enabled")
    private String smsEnabled;
    @Column(name = "email_enabled")
    private String emailEnabled;
    @Lob
    @Column(name = "sms_body")
    private String smsBody;
    @Lob
    @Column(name = "email_body")
    private String emailBody;
    @Column(name = "included_flagged_rules")
    private String includedFlaggedRules;
    @Column(name = "frm_enabled")
    private String frmEnabled;
    @Lob
    @Column(name = "frm_body")
    private String frmBody;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reactionTemplateId")
    private Collection<RuleGroup> ruleGroupCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reactionTemplateId")
    private Collection<ReactionTemplateRole> reactionTemplateRoleCollection;
    public ReactionTemplate() {
    }
    public ReactionTemplate(Integer reactionTemplateId) {
        this.reactionTemplateId = reactionTemplateId;
    }
    public ReactionTemplate(Integer reactionTemplateId, String templateUuid, String templateName) {
        this.reactionTemplateId = reactionTemplateId;
        this.templateUuid = templateUuid;
        this.templateName = templateName;
    }
    public Integer getReactionTemplateId() {
        return reactionTemplateId;
    }
    public void setReactionTemplateId(Integer reactionTemplateId) {
        this.reactionTemplateId = reactionTemplateId;
    }
    public String getTemplateUuid() {
        return templateUuid;
    }
    public void setTemplateUuid(String templateUuid) {
        this.templateUuid = templateUuid;
    }
    public String getTemplateName() {
        return templateName;
    }
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
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
    public String getSmsBody() {
        return smsBody;
    }
    public void setSmsBody(String smsBody) {
        this.smsBody = smsBody;
    }
    public String getEmailBody() {
        return emailBody;
    }
    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }
    public String getIncludedFlaggedRules() {
        return includedFlaggedRules;
    }
    public void setIncludedFlaggedRules(String includedFlaggedRules) {
        this.includedFlaggedRules = includedFlaggedRules;
    }
    public String getFrmEnabled() {
        return frmEnabled;
    }
    public void setFrmEnabled(String frmEnabled) {
        this.frmEnabled = frmEnabled;
    }
    public String getFrmBody() {
        return frmBody;
    }
    public void setFrmBody(String frmBody) {
        this.frmBody = frmBody;
    }
    public Collection<RuleGroup> getRuleGroupCollection() {
        return ruleGroupCollection;
    }
    public void setRuleGroupCollection(Collection<RuleGroup> ruleGroupCollection) {
        this.ruleGroupCollection = ruleGroupCollection;
    }
    public Collection<ReactionTemplateRole> getReactionTemplateRoleCollection() {
        return reactionTemplateRoleCollection;
    }
    public void setReactionTemplateRoleCollection(Collection<ReactionTemplateRole> reactionTemplateRoleCollection) {
        this.reactionTemplateRoleCollection = reactionTemplateRoleCollection;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reactionTemplateId != null ? reactionTemplateId.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ReactionTemplate)) {
            return false;
        }
        ReactionTemplate other = (ReactionTemplate) object;
        if ((this.reactionTemplateId == null && other.reactionTemplateId != null) || (this.reactionTemplateId != null && !this.reactionTemplateId.equals(other.reactionTemplateId))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "hibernateexample.ReactionTemplate[ reactionTemplateId=" + reactionTemplateId + " ]";
    }
}
