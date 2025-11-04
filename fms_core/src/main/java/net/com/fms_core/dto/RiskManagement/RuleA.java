/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.dto.RiskManagement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.com.fms_core.config.RuleStore;
import java.util.List;

@Data
public class RuleA {
    List<String> rules;
    List<String> velocityRules;
    public RuleA() {
        this.rules = RuleStore.getRules();
        this.velocityRules = RuleStore.getVelocityRules();
    }
    public RuleA(List<String> rules) {
        this.rules = rules;
    }
    public void setRules(List<String> rules) {
        this.rules = rules;
    }
    public List<String> getRules() {
        return rules;
    }
    public List<String> getVelocityRules() {
        return velocityRules;
    }
}
