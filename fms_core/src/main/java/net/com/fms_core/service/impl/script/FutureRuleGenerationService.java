package net.com.fms_core.service.impl.script;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */

@Slf4j
@Service
public class FutureRuleGenerationService {

    public String generateRules(String csvPath) {
        StringBuilder rulesContent = new StringBuilder();
        List<String> output = new ArrayList<>();
        String pythonCommand = "python";
        String scriptPath = "C:\\Users\\kavinda_d\\Documents\\e soft\\final project\\project\\fms backend\\fms_core\\src\\main\\java\\net\\com\\fms_core\\script\\rulegenerator\\future_rule_generator.py";
        
        try {
            ProcessBuilder pb = new ProcessBuilder(pythonCommand, scriptPath, csvPath);
            pb.directory(new java.io.File("C:\\Users\\kavinda_d\\Documents\\e soft\\final project\\project\\fms backend\\fms_core\\src\\main\\java\\net\\com\\fms_core\\script\\rulegenerator"));
            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            boolean captureRules = false;
            
            while ((line = reader.readLine()) != null) {
                output.add(line);
                
                if (line.equals("===RULES_START===")) {
                    captureRules = true;
                    continue;
                }
                if (line.equals("===RULES_END===")) {
                    captureRules = false;
                    continue;
                }
                if (captureRules) {
                    rulesContent.append(line).append("\n");
                }
            }

            process.waitFor();
            log.info("Python script output: {}", output);

        } catch (Exception e) {
            log.error("Failed to generate future rules: {}", e.getMessage(), e);
            return "Error: Failed to generate rules - " + e.getMessage();
        }

        return rulesContent.toString();
    }
}
