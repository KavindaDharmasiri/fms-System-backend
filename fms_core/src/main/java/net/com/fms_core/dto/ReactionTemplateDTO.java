/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReactionTemplateDTO {
    private Integer reactionTemplateId;
    private String templateUuid;
    private String templateName;
    private String subject;
    private String status;
    private String smsEnabled;
    private String emailEnabled;
    private String smsBody;
    private String emailBody;
    private String includedFlaggedRules;
    private String frmEnabled;
    private String frmBody;
    private String user;
    private List<Integer> roleids;
    private Collection<RuleGroupDTO> ruleGroupCollection;
    private Collection<ReactionTemplateRoleDTO> reactionTemplateRoleCollection;
}
