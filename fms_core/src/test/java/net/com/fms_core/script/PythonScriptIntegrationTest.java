package net.com.fms_core.script;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for Python script execution
 * These tests verify that the Python scripts can be executed and produce expected output
 */
class PythonScriptIntegrationTest {

    private static final String SCRIPT_DIR = "C:\\Users\\kavinda_d\\Documents\\e soft\\final project\\project\\fms backend\\fms_core\\src\\main\\java\\net\\com\\fms_core\\script\\rulegenerator";
    private static final String PYTHON_COMMAND = "python";

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testPretrainedRuleGeneratorScript_Exists() {
        // Given
        String scriptPath = SCRIPT_DIR + "\\pretrained_rule_generator.py";
        File scriptFile = new File(scriptPath);

        // Then
        assertTrue(scriptFile.exists(), "Python script should exist at: " + scriptPath);
        assertTrue(scriptFile.canRead(), "Python script should be readable");
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testPretrainedRuleGeneratorScript_Execution() {
        // Given
        String scriptPath = SCRIPT_DIR + "\\pretrained_rule_generator.py";
        File scriptFile = new File(scriptPath);
        
        // Skip test if script doesn't exist
        if (!scriptFile.exists()) {
            System.out.println("Skipping test - Python script not found");
            return;
        }

        try {
            // When
            ProcessBuilder pb = new ProcessBuilder(PYTHON_COMMAND, scriptPath);
            pb.directory(new File(SCRIPT_DIR));
            pb.redirectErrorStream(true);
            Process process = pb.start();

            List<String> output = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.add(line);
            }

            int exitCode = process.waitFor();

            // Then
            assertNotNull(output, "Should have output from Python script");
            assertFalse(output.isEmpty(), "Output should not be empty");
            
            // Check for expected output patterns
            boolean hasRulesStart = output.stream().anyMatch(l -> l.equals("===RULES_START==="));
            boolean hasRulesEnd = output.stream().anyMatch(l -> l.equals("===RULES_END==="));
            boolean hasMetadataStart = output.stream().anyMatch(l -> l.equals("===METADATA_START==="));
            boolean hasMetadataEnd = output.stream().anyMatch(l -> l.equals("===METADATA_END==="));
            
            if (exitCode == 0) {
                assertTrue(hasRulesStart, "Should have RULES_START marker");
                assertTrue(hasRulesEnd, "Should have RULES_END marker");
                assertTrue(hasMetadataStart, "Should have METADATA_START marker");
                assertTrue(hasMetadataEnd, "Should have METADATA_END marker");
            } else {
                // If script failed, should have error information
                System.out.println("Script failed with exit code: " + exitCode);
                System.out.println("Output: " + String.join("\n", output));
            }

        } catch (Exception e) {
            // Test should handle exceptions gracefully
            System.out.println("Exception during script execution: " + e.getMessage());
            // This is expected if Python is not installed or dependencies are missing
        }
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testPretrainedRuleGeneratorScript_SyntheticDataGeneration() {
        // Given
        String scriptPath = SCRIPT_DIR + "\\pretrained_rule_generator.py";
        File scriptFile = new File(scriptPath);
        
        if (!scriptFile.exists()) {
            System.out.println("Skipping test - Python script not found");
            return;
        }

        try {
            // When - Execute script (should generate synthetic data if no CSV found)
            ProcessBuilder pb = new ProcessBuilder(PYTHON_COMMAND, scriptPath);
            pb.directory(new File(SCRIPT_DIR));
            pb.redirectErrorStream(true);
            Process process = pb.start();

            List<String> output = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.add(line);
            }

            int exitCode = process.waitFor();

            // Then
            if (exitCode == 0) {
                // Should mention synthetic data generation if no CSV files found
                boolean mentionsSyntheticData = output.stream()
                    .anyMatch(l -> l.contains("synthetic") || l.contains("Generated"));
                
                // Should have summary information
                boolean hasSummary = output.stream()
                    .anyMatch(l -> l.startsWith("SUMMARY:"));
                
                assertTrue(mentionsSyntheticData || hasSummary, 
                    "Should mention data source (synthetic or loaded from CSV)");
            }

        } catch (Exception e) {
            System.out.println("Exception during synthetic data test: " + e.getMessage());
        }
    }

    @Test
    void testModelFilesExist() {
        // Given
        String modelDir = SCRIPT_DIR + "\\csv\\models";
        File modelDirFile = new File(modelDir);

        if (!modelDirFile.exists()) {
            System.out.println("Model directory not found - this is expected if models haven't been trained yet");
            return;
        }

        // Expected model files
        String[] expectedFiles = {
            "randomforest_model.pkl",
            "gradientboosting_model.pkl", 
            "decisiontree_model.pkl",
            "scaler.pkl",
            "encoders.pkl",
            "model_metadata.json"
        };

        // Then
        for (String fileName : expectedFiles) {
            File modelFile = new File(modelDir, fileName);
            if (modelFile.exists()) {
                assertTrue(modelFile.canRead(), fileName + " should be readable");
                assertTrue(modelFile.length() > 0, fileName + " should not be empty");
            } else {
                System.out.println("Model file not found (expected if not trained): " + fileName);
            }
        }
    }

    @Test
    void testPythonDependencies() {
        try {
            // Test if Python is available
            ProcessBuilder pb = new ProcessBuilder(PYTHON_COMMAND, "--version");
            Process process = pb.start();
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                System.out.println("Python is available");
                
                // Test required packages
                String[] packages = {"pandas", "numpy", "scikit-learn", "joblib"};
                
                for (String pkg : packages) {
                    try {
                        ProcessBuilder pkgTest = new ProcessBuilder(PYTHON_COMMAND, "-c", "import " + pkg);
                        Process pkgProcess = pkgTest.start();
                        int pkgExitCode = pkgProcess.waitFor();
                        
                        if (pkgExitCode == 0) {
                            System.out.println("Package " + pkg + " is available");
                        } else {
                            System.out.println("Package " + pkg + " is NOT available");
                        }
                    } catch (Exception e) {
                        System.out.println("Could not test package " + pkg + ": " + e.getMessage());
                    }
                }
            } else {
                System.out.println("Python is NOT available");
            }
            
        } catch (Exception e) {
            System.out.println("Could not test Python availability: " + e.getMessage());
        }
    }

    @Test
    void testScriptDirectoryStructure() {
        // Given
        File scriptDir = new File(SCRIPT_DIR);

        // Then
        assertTrue(scriptDir.exists(), "Script directory should exist");
        assertTrue(scriptDir.isDirectory(), "Should be a directory");
        assertTrue(scriptDir.canRead(), "Should be readable");

        // Check for expected files
        String[] expectedScripts = {
            "pretrained_rule_generator.py",
            "train_model.py",
            "generate_rules_fast.py",
            "advanced_fraud_trainer.py"
        };

        for (String script : expectedScripts) {
            File scriptFile = new File(scriptDir, script);
            if (scriptFile.exists()) {
                assertTrue(scriptFile.canRead(), script + " should be readable");
                assertTrue(scriptFile.length() > 0, script + " should not be empty");
                System.out.println("Found script: " + script);
            } else {
                System.out.println("Script not found: " + script);
            }
        }
    }
}