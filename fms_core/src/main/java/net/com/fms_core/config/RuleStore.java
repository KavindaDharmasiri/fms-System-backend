/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.config;
import lombok.AllArgsConstructor;
import net.com.fms_core.service.FmsRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class RuleStore {
    static FmsRuleService FmsRuleService;
    @Autowired
    public void setFmsRuleService(FmsRuleService service) {
        FmsRuleService = service;
    }
    public static List<String> getRules() {
        return FmsRuleService.getFinalRules();
    }
    public static List<String> getVelocityRulesTest() {
        return List.of(
                "rule \"Test Rule\"\n" +
                        "when\n" +
                        "    eval(true)\n" +
                        "then\n" +
                        "    System.out.println(\"✅ Rule Engine is working!\");\n" +
                        "end",
                "import net.com.fms_core.dto.RiskManagement.Transaction;\n" +
                        "import com.com.rules.config.TransactionHistory;\n" +
                        "import java.util.List;\n" +
                        "\n" +
                "rule \"Velocity Check - Too Many Transactions in Short Time xxx \"\n" +
                        "when\n" +
                        "    $t : Transaction()\n" +
                        "    $recentTxns : List(size > 5) from collect (Transaction() from TransactionHistory.getRecentTransactions($t.getUserId()))\n" +
                        "then\n" +
                        "    $t.setRiskLevel(\"HIGH\");\n" +
                        "    $t.setFlaggedForReview(true);\n" +
                        "    System.out.println(\"\uD83D\uDEA8 HIGH RISK: More than 5 xxx transactions detected in a short period for user \" + $t.getUserId());\n" +
                        "end",
                "import net.com.fms_core.dto.RiskManagement.Transaction;\n" +
                        "import com.com.rules.config.TransactionHistory;\n" +
                        "import java.util.List;\n" +
                        "\n" +
                        "rule \"Velocity Check - Debug Mode ud\"\n" +
                        "when\n" +
                        "    $t : Transaction()\n" +
                        "then\n" +
                        "    System.out.println(\"\uD83D\uDE80 Rule checked for user: \" + $t.getUserId());\n" +
                        "    \n" +
                        "    // Manually get transactions inside 'then' to confirm they exist\n" +
                        "    List<Transaction> recentTxns = TransactionHistory.getRecentTransactions($t.getUserId());\n" +
                        "    System.out.println(\"Transactions found: \" + recentTxns.size());\n" +
                        "    System.out.println(\"Transaction details: \" + recentTxns);\n" +
                        "end",
                "import net.com.fms_core.dto.RiskManagement.Transaction;\n" +
                        "import com.com.rules.config.TransactionHistory;\n" +
                        "import java.util.List;\n" +
                        "\n" +
                        "rule \"Velocity Check - Debug Mode\"\n" +
                        "when\n" +
                        "    $t : Transaction()\n" +
                        "    $recentTxns : List() from TransactionHistory.getRecentTransactions($t.getUserId())\n" +
                        "then\n" +
                        "    System.out.println(\"\uD83D\uDE80 Rule checked for user: \" + $t.getUserId());\n" +
                        "    System.out.println(\"Transactions found: \" + $recentTxns.size());\n" +
                        "    System.out.println(\"Transaction details: \" + $recentTxns);\n" +
                        "end",
                "import net.com.fms_core.dto.RiskManagement.Transaction;\n" +
                        "rule \"High-Risk Transaction - Large Amount - 001\"\n" +
                        "when\n" +
                        "    $t : Transaction(amount > 100, customerRiskScore > 7)\n" +
                        "then\n" +
                        "    $t.setRiskLevel(\"HIGH\");\n" +
                        "    $t.setFlaggedForReview(true);\n" +
                        "    System.out.println(\"HIGH RISK: More than 5 transactions detected in a short period for user \" + $t.getUserId());\n" +
                        "    System.out.println(\"Transaction flagged as HIGH RISK due to large amount and high customer risk score.\");\n" +
                        "end",
                "import net.com.fms_core.dto.RiskManagement.Transaction;\n" +
                        "import java.util.List;\n" +
                        "import java.util.Arrays;\n" +
                        "\n" +
                        "rule \"Test Velocity Rule\"\n" +
                        "when\n" +
                        "    $t : Transaction(userId == \"user123\") // Test user\n" +
                        "then\n" +
                        "    System.out.println(\"TEST SUCCESS: Rule fired for user \" + $t.getUserId());\n" +
                        "end",
                "import net.com.fms_core.dto.RiskManagement.Transaction;\n" +
                        "import com.com.rules.config.TransactionHistory;\n" +
                        "import java.util.List;\n" +
                        "\n" +
                        "rule \"Velocity Check - Too Many Transactions in Short Time UD\"\n" +
                        "when\n" +
                        "    $t : Transaction(amount > 100, customerRiskScore > 7)\n" +
                        "    $recentTxns : List() from TransactionHistory.getRecentTransactions($t.getUserId()) // Fetch transactions\n" +
                        "then\n" +
                        "    System.out.println(\"DEBUG: Rule triggered for user \" + $t.getUserId());\n" +
                        "    System.out.println(\"DEBUG: Recent Transactions -> \" + $recentTxns);\n" +
                        "    \n" +
                        "    $t.setRiskLevel(\"HIGH\");\n" +
                        "    $t.setFlaggedForReview(true);\n" +
                        "\n" +
                        "    System.out.println(\"HIGH RISK: More than 5 transactions detected in a short period for user \" + $t.getUserId());\n" +
                        "end",
                "package rules;\n" +
                        "\n" +
                        "import net.com.fms_core.dto.RiskManagement.Transaction;\n" +
                        "import com.com.rules.config.TransactionHistory;\n" +
                        "import java.util.List;\n" +
                        "\n" +
                        "rule \"Velocity Check - Too Many Transactions in Short Time\"\n" +
                        "when\n" +
                        "    $t : Transaction()\n" +
                        "    $recentTxns : List(size > 5) from TransactionHistory.getRecentTransactions($t.getUserId())\n" +
                        "then\n" +
                        "    $t.setRiskLevel(\"HIGH\");\n" +
                        "    $t.setFlaggedForReview(true);\n" +
                        "    System.out.println(\"HIGH RISK: More than 5 transactions detected in a short period for user \" + $t.getUserId());\n" +
                        "end",
                "package rules;\n" +
                        "import net.com.fms_core.dto.RiskManagement.Transaction;\n" +
                        "import java.util.List;\n" +
                        "import java.util.Date;\n" +
                        "rule \"Velocity Check - Large Spending in Short Time\"\n" +
                        "when\n" +
                        "    $t : Transaction()\n" +
                        "    $recentTxns : List() from TransactionHistory.getRecentTransactions($t.getUserId())\n" +
                        "    $totalAmount : Number() from accumulate (\n" +
                        "        Transaction(amount : amount) from $recentTxns, \n" +
                        "        sum(amount)\n" +
                        "    )\n" +
                        "    eval($totalAmount.doubleValue() > 500)\n" +
                        "then\n" +
                        "    $t.setRiskLevel(\"HIGH\");\n" +
                        "    $t.setFlaggedForReview(true);\n" +
                        "    System.out.println(\"HIGH RISK: Total spending exceeds $500 in a short period.\");\n" +
                        "end"
        );
    }
    public static List<String> getVelocityRules() {
        return List.of(
                "rule \"Test Rule\"\n" +
                        "when\n" +
                        "    eval(true)\n" +
                        "then\n" +
                        "    System.out.println(\"✅ Rule Engine is working!\");\n" +
                        "end",
                "import net.com.fms_core.dto.RiskManagement.Transaction;\n" +
                        "import com.com.rules.config.TransactionHistory;\n" +
                        "import java.util.List;\n" +
                        "\n" +
                        "rule \"Velocity Check - Too Many Transactions in Short Time xxx \"\n" +
                        "when\n" +
                        "    $t : Transaction()\n" +
                        "    $recentTxns : List(size > 5) from collect (Transaction() from TransactionHistory.getRecentTransactions($t.getUserId()))\n" +
                        "then\n" +
                        "    $t.setRiskLevel(\"HIGH\");\n" +
                        "    $t.setFlaggedForReview(true);\n" +
                        "    System.out.println(\"\uD83D\uDEA8 HIGH RISK: More than 5 xxx transactions detected in a short period for user \" + $t.getUserId());\n" +
                        "end",
                "import net.com.fms_core.dto.RiskManagement.Transaction;\n" +
                        "import com.com.rules.config.TransactionHistory;\n" +
                        "import java.util.List;\n" +
                        "\n" +
                        "rule \"Velocity Check - Debug Mode ud\"\n" +
                        "when\n" +
                        "    $t : Transaction()\n" +
                        "then\n" +
                        "    System.out.println(\"\uD83D\uDE80 Rule checked for user: \" + $t.getUserId());\n" +
                        "    \n" +
                        "    // Manually get transactions inside 'then' to confirm they exist\n" +
                        "    List<Transaction> recentTxns = TransactionHistory.getRecentTransactions($t.getUserId());\n" +
                        "    System.out.println(\"Transactions found: \" + recentTxns.size());\n" +
                        "    System.out.println(\"Transaction details: \" + recentTxns);\n" +
                        "end",
                "import net.com.fms_core.dto.RiskManagement.Transaction;\n" +
                        "import com.com.rules.config.TransactionHistory;\n" +
                        "import java.util.List;\n" +
                        "import java.time.Duration;\n" +
                        "import java.time.temporal.ChronoUnit;\n"+
                        "\n" +
                "rule \"Velocity Check - Too Many Transactions in 10 Minutes\"\n" +
                        "when\n" +
                        "    $t : Transaction()\n" +
                        "    $recentTxns : List(size > 5) from collect (\n" +
                        "        Transaction(timeOfTransaction.isAfter($t.getTimeOfTransaction().minus(Duration.ofMillis(600000)))) \n" +
                        "        from TransactionHistory.getRecentTransactions($t.getUserId())\n" +
                        "    )\n" +
                        "then\n" +
                        "    $t.setRiskLevel(\"HIGH\");\n" +
                        "    $t.setFlaggedForReview(true);\n" +
                        "    System.out.println(\"\uD83D\uDEA8 HIGH RISK: More than 5 transactions detected within 10 minutes for user \" + $t.getUserId());\n" +
                        "end",
                "import net.com.fms_core.dto.RiskManagement.Transaction;\n" +
                        "import com.com.rules.config.TransactionHistory;\n" +
                        "import java.util.List;\n" +
                        "import java.time.Duration;\n" +
                        "import java.time.temporal.ChronoUnit;\n"+
                        "\n" +
                "rule \"Velocity Check - High Spending in 30 Minutes\"\n" +
                        "when\n" +
                        "    $t : Transaction()\n" +
                        "    $totalAmount : Number() from accumulate (\n" +
                        "        Transaction(amount : amount, timeOfTransaction.isAfter($t.getTimeOfTransaction().minus(Duration.ofMillis(1800000)))) \n" +
                        "        from TransactionHistory.getRecentTransactions($t.getUserId()), \n" +
                        "        sum(amount)\n" +
                        "    )\n" +
                        "    eval($totalAmount.doubleValue() > 500)\n" +
                        "then\n" +
                        "    $t.setRiskLevel(\"HIGH\");\n" +
                        "    $t.setFlaggedForReview(true);\n" +
                        "    System.out.println(\"\uD83D\uDEA8 HIGH RISK: Total spending exceeds $500 in 30 minutes for user \" + $t.getUserId());\n" +
                        "end",
                "import net.com.fms_core.dto.RiskManagement.Transaction;\n" +
                        "import com.com.rules.config.TransactionHistory;\n" +
                        "import java.util.List;\n" +
                        "import java.time.Duration;\n" +
                        "import java.time.temporal.ChronoUnit;\n"+
                        "\n" +
                "rule \"Velocity Check - Frequent Transactions by Same Card/User\"\n" +
                        "when\n" +
                        "    $t : Transaction()\n" +
                        "    $recentTxns : List(size > 3) from collect (\n" +
                        "        Transaction(cardNumber == $t.getCardNumber(), timeOfTransaction.isAfter($t.getTimeOfTransaction().minus(Duration.ofMillis(300000)))) \n" +
                        "        from TransactionHistory.getRecentTransactions($t.getUserId())\n" +
                        "    )\n" +
                        "then\n" +
                        "    $t.setRiskLevel(\"HIGH\");\n" +
                        "    $t.setFlaggedForReview(true);\n" +
                        "    System.out.println(\"\uD83D\uDEA8 HIGH RISK: Frequent transactions detected within 5 minutes for card \" + $t.getCardNumber());\n" +
                        "end"
        );
    }
}
