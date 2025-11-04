/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.config;
import net.com.fms_core.controller.RiskController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {
    private final RiskController riskController;
    public StartupRunner(RiskController riskController) {
        this.riskController = riskController;
    }
    @Override
    public void run(String... args) throws Exception {
        riskController.setRules();
    }
}
