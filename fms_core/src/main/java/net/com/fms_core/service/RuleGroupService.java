/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.service;
import net.com.fms_core.dto.ApiPageReqDTO;
import net.com.fms_core.dto.RuleGroupDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface RuleGroupService {
    RuleGroupDTO saveRuleGroup(RuleGroupDTO ruleGroupDTO);
    RuleGroupDTO updateRuleGroup(RuleGroupDTO ruleGroupDTO);
    RuleGroupDTO getRuleGroup(Integer ruleGroupId);
    Page<RuleGroupDTO> filterRuleGroups(RuleGroupDTO ruleGroupDTO, ApiPageReqDTO pageable);
    SseEmitter testRuleGroup(RuleGroupDTO ruleGroupDTO);
    String deleteRuleGroup(Integer ruleGroupId);
}
