/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.config;
import net.com.fms_core.controller.RiskController;
import net.com.fms_core.entity.AIRule;
import net.com.fms_core.repository.AIRuleRepository;
import net.com.fms_core.service.impl.AIKieService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class StartupRunner implements CommandLineRunner {
    private final RiskController riskController;
    private final AIKieService aiKieService;
    private final AIRuleRepository aiRuleRepository;

    public StartupRunner(RiskController riskController, AIKieService aiKieService, AIRuleRepository aiRuleRepository) {
        this.riskController = riskController;
        this.aiKieService = aiKieService;
        this.aiRuleRepository = aiRuleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("StartupRunner: Initializing manual rules...");
        // Initialize manual rules KIE base
        riskController.setRules();

        System.out.println("StartupRunner: Initializing AI rules...");
        // Initialize AI rules KIE base with deployed AI rules
        initializeAIRules();
        System.out.println("StartupRunner: Initialization complete.");
    }

    private void initializeAIRules() {
        try {
            List<AIRule> deployedAIRules = aiRuleRepository.findDeployedRulesOrderByPriority();
            if (!deployedAIRules.isEmpty()) {
                aiKieService.deployAIRules(deployedAIRules);
                System.out.println("Initialized AI KIE base with " + deployedAIRules.size() + " deployed AI rules");
            } else {
                System.out.println("No deployed AI rules found - AI KIE base initialized with default rule");
            }
        } catch (Exception e) {
            System.err.println("Failed to initialize AI rules: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
