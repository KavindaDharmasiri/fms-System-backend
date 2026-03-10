/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.service;
import net.com.fms_core.dto.ApiPageReqDTO;
import net.com.fms_core.dto.FmsRuleDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.List;

public interface FmsRuleService {
    FmsRuleDTO saveRule(FmsRuleDTO FmsRuleDTO);
    FmsRuleDTO updateRule(FmsRuleDTO FmsRuleDTO);
    FmsRuleDTO getRule(Integer fmsRuleId);
    Page<FmsRuleDTO> filterFmsRules(FmsRuleDTO FmsRuleDTO, ApiPageReqDTO pageable);
    List<String> getFinalRules();
    SseEmitter testRule(FmsRuleDTO FmsRuleDTO);
    String deleteRule(Integer fmsRuleId);
}
