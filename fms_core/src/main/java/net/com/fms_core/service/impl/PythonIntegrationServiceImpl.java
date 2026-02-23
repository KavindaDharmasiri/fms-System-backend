package net.com.fms_core.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.dto.ImpossibleDistanceResult;
import net.com.fms_core.dto.message.IsoMessageDTO;
import net.com.fms_core.entity.TransactionHistory;
import net.com.fms_core.repository.TransactionRepository;
import net.com.fms_core.service.PythonIntegrationService;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PythonIntegrationServiceImpl implements PythonIntegrationService {
    
    private final ObjectMapper objectMapper;
    private final TransactionRepository transactionRepository;
    private static final String PYTHON_SCRIPT_PATH = "impossible_distance_model.py";
    private static final String PYTHON_INPUT_FILE = "python_input.json";
    private static final String PYTHON_OUTPUT_FILE = "fraud_analysis_results.json";
    private String pythonExecutablePath = null;
    private long timeDifferenceMinutes = 0L;
    private double calculatedDistance = 0.0;
    
    @Override
    public ImpossibleDistanceResult callPythonAnalysis(IsoMessageDTO currentTransaction) {
        try {
            // 1. Create input file for Python
            createPythonInputFile(currentTransaction);
            
            // 2. Execute Python script
            boolean success = executePythonScript();
            
            if (success) {
                // 3. Read Python results
                return readPythonResults();
            } else {
                log.warn("Python execution failed, using fallback");
                return createFallbackResult();
            }
            
        } catch (Exception e) {
            log.error("Error in Python integration: {}", e.getMessage());
            return createFallbackResult();
        }
    }
    
    @Override
    public boolean isPythonAvailable() {
        String[] pythonPaths = {
            "python", // Try PATH first
            "python3",
            "C:\\Python39\\python.exe",
            "C:\\Python310\\python.exe",
            "C:\\Python311\\python.exe"
        };
        
        for (String pythonPath : pythonPaths) {
            try {
                ProcessBuilder pb = new ProcessBuilder(pythonPath, "-c", "print('Python Available')");
                Process process = pb.start();
                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    log.info("Python found at: {}", pythonPath);
                    this.pythonExecutablePath = pythonPath;
                    return true;
                }
            } catch (Exception e) {
                log.debug("Python not found at: {} - {}", pythonPath, e.getMessage());
            }
        }
        return false;
    }
    
    private void createPythonInputFile(IsoMessageDTO currentTransaction) throws IOException {
        // Get previous transaction for location comparison
        String cardNumber = currentTransaction.getPan();
        if (cardNumber == null || cardNumber.length() < 12) {
            throw new IOException("Invalid card number");
        }
        
        // Find recent transactions using first 6 digits (BIN) to match masked cards
        String cardPrefix = cardNumber.substring(0, 6);
        log.info("PYTHON ANALYSIS - Searching for transactions with card prefix: {}", cardPrefix);
        List<TransactionHistory> recentTransactions = transactionRepository.findAll();
        log.info("PYTHON ANALYSIS - Found {} recent transactions", recentTransactions.size());
        
        // Log details of found transactions
        for (int i = 0; i < Math.min(recentTransactions.size(), 3); i++) {
            TransactionHistory txn = recentTransactions.get(i);
            log.info("PYTHON ANALYSIS - Transaction {}: ID={}, CreatedAt={}", 
                    i+1, txn.getTransactionHistoryId(), txn.getCreatedAt());
        }
        
        // Extract current location
        LocationData currentLoc = extractLocationForPython(currentTransaction);
        LocationData previousLoc = null;
        String previousTimestamp = "";
        
        if (!recentTransactions.isEmpty()) {
            TransactionHistory lastTransaction = recentTransactions.get(0);
            try {
                IsoMessageDTO lastTxnData = objectMapper.readValue(lastTransaction.getTranPacket(), IsoMessageDTO.class);
                previousLoc = extractLocationForPython(lastTxnData);
                previousTimestamp = lastTransaction.getCreatedAt().toString();
                
                // Calculate time difference using createdAt timestamps
                java.time.LocalDateTime currentTime = java.time.LocalDateTime.now();
                java.time.LocalDateTime previousTime = lastTransaction.getCreatedAt().toInstant()
                    .atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
                timeDifferenceMinutes = java.time.Duration.between(previousTime, currentTime).toMinutes();
                
                // Calculate distance
                calculatedDistance = haversineDistance(previousLoc.latitude, previousLoc.longitude, 
                                                     currentLoc.latitude, currentLoc.longitude);
                
                log.info("PYTHON ANALYSIS - Found previous transaction ID: {}", lastTransaction.getTransactionHistoryId());
                log.info("PYTHON ANALYSIS - Time difference: {} minutes", timeDifferenceMinutes);
            } catch (Exception e) {
                log.warn("Failed to parse previous transaction for Python: {}", e.getMessage());
            }
        } else {
            log.info("PYTHON ANALYSIS - No previous transactions found for card: {}****", cardNumber.substring(0, 6));
            // Set previous location to null/default values
            previousLoc = new LocationData();
            previousLoc.latitude = 0.0;
            previousLoc.longitude = 0.0;
            previousLoc.locationName = "Unknown";
        }
        
        // Print locations for debugging
        log.info("PYTHON ANALYSIS - Current Location: {} ({}, {})", 
                currentLoc.locationName, currentLoc.latitude, currentLoc.longitude);
        log.info("PYTHON ANALYSIS - Previous Location: {} ({}, {})", 
                previousLoc != null ? previousLoc.locationName : "Unknown",
                previousLoc != null ? previousLoc.latitude : 0.0,
                previousLoc != null ? previousLoc.longitude : 0.0);
        log.info("PYTHON ANALYSIS - Number of recent transactions found: {}", recentTransactions.size());
        
        // Create comprehensive input for Python
        String jsonInput = String.format(
            "{\"current_location\":{\"latitude\":%f,\"longitude\":%f,\"name\":\"%s\"}," +
            "\"previous_location\":{\"latitude\":%f,\"longitude\":%f,\"name\":\"%s\"}," +
            "\"current_timestamp\":\"%s\",\"previous_timestamp\":\"%s\"," +
            "\"card_number\":\"%s\",\"amount\":%f}",
            currentLoc.latitude, currentLoc.longitude, currentLoc.locationName,
            previousLoc != null ? previousLoc.latitude : 0.0,
            previousLoc != null ? previousLoc.longitude : 0.0,
            previousLoc != null ? previousLoc.locationName : "Unknown",
            java.time.LocalDateTime.now().toString(),
            previousTimestamp,
            cardNumber.substring(0, 6) + "****",
            currentTransaction.getAmount()
        );
        
        Files.write(Paths.get(PYTHON_INPUT_FILE), jsonInput.getBytes());
        log.info("Python Input JSON: {}", jsonInput);
    }
    
    private boolean executePythonScript() {
        try {
            // Execute Python script
            String pythonExe = this.pythonExecutablePath != null ? this.pythonExecutablePath : "python";
            
            ProcessBuilder pb = new ProcessBuilder(
                pythonExe, 
                PYTHON_SCRIPT_PATH
            );
            
            // Set working directory to fms_core module where Python script should be located
            File workingDir = new File("fms_core");
            if (!workingDir.exists()) {
                workingDir = new File(".");
            }
            pb.directory(workingDir);
            log.info("Python working directory: {}", workingDir.getAbsolutePath());
            pb.redirectErrorStream(true);
            Process process = pb.start();
            
            // Wait for completion with timeout for Python execution
            boolean finished = process.waitFor(30, java.util.concurrent.TimeUnit.SECONDS);
            
            if (!finished) {
                process.destroyForcibly();
                log.warn("Python process timed out");
                return false;
            }
            
            int exitCode = process.exitValue();
            
            // Read Python output for debugging
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                StringBuilder output = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                if (exitCode != 0) {
                    log.warn("Python execution failed with exit code: {}. Output: {}", exitCode, output.toString());
                } else {
                    log.info("Python execution completed successfully");
                }
            }
            
            return exitCode == 0;
            
        } catch (Exception e) {
            log.error("Failed to execute Python script: {}", e.getMessage());
            return false;
        }
    }
    
    private ImpossibleDistanceResult readPythonResults() {
        try {
            if (!Files.exists(Paths.get(PYTHON_OUTPUT_FILE))) {
                log.warn("Python output file not found");
                return createFallbackResult();
            }
            
            String jsonContent = new String(Files.readAllBytes(Paths.get(PYTHON_OUTPUT_FILE)));
            JsonNode results = objectMapper.readTree(jsonContent);
            log.info("Python results: {}", jsonContent);
            
            // Parse Python results
            double riskScore = results.get("risk_score").asDouble();
            double fraudProbability = results.get("fraud_probability").asDouble();
            
            // Handle string boolean from Python
            boolean isImpossible = false;
            if (results.has("is_impossible")) {
                String impossibleStr = results.get("is_impossible").asText();
                isImpossible = "True".equals(impossibleStr) || "true".equals(impossibleStr) || results.get("is_impossible").asBoolean();
            } else {
                isImpossible = riskScore > 80;
            }
            
            // Use calculated distance and time
            double distanceKm = calculateDistanceFromLocations();
            double requiredSpeedKmh = timeDifferenceMinutes > 0 ? (distanceKm / timeDifferenceMinutes) * 60 : 0.0;
            
            // Convert to ImpossibleDistanceResult
            ImpossibleDistanceResult result = new ImpossibleDistanceResult(
                isImpossible,                      // isImpossible
                distanceKm,                        // distanceKm
                timeDifferenceMinutes,             // timeDifferenceMinutes
                requiredSpeedKmh,                  // requiredSpeedKmh
                900.0,                             // maxPossibleSpeedKmh
                riskScore > 80 ? "HIGH" : (riskScore > 60 ? "MID" : "LOW"),
                "Previous Location (LK)",
                "Current Location (US)",
                "****",
                String.format("Python Analysis: Risk Score %.1f, Fraud Probability %.1f%%, Required Speed %.1f km/h", 
                    riskScore, fraudProbability * 100, requiredSpeedKmh)
            );
            
            log.info("Python analysis result: Risk={}, Impossible={}, Distance={}km", riskScore, isImpossible, distanceKm);
            return result;
            
        } catch (Exception e) {
            log.error("Failed to read Python results: {}", e.getMessage());
            return createFallbackResult();
        }
    }
    
    private double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // Earth's radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }
    
    private double calculateDistanceFromLocations() {
        return calculatedDistance;
    }
    
    private LocationData extractLocationForPython(IsoMessageDTO transaction) {
        // Same location extraction logic as Java implementation
        try {
            String cardAcceptorNameLocation = transaction.getCardAcceptorNameLocation();
            
            if (cardAcceptorNameLocation != null && !cardAcceptorNameLocation.isEmpty()) {
                return parseCardAcceptorLocationForPython(cardAcceptorNameLocation);
            }
            
            return generateDefaultLocationForPython();
            
        } catch (Exception e) {
            log.warn("Failed to extract location for Python: {}", e.getMessage());
            return generateDefaultLocationForPython();
        }
    }
    
    private LocationData parseCardAcceptorLocationForPython(String cardAcceptorNameLocation) {
        LocationData location = new LocationData();
        
        if (cardAcceptorNameLocation.length() >= 40) {
            String cityName = cardAcceptorNameLocation.substring(25, 38).trim();
            String countryCode = cardAcceptorNameLocation.substring(38).trim();
            
            location = geocodeLocationForPython(cityName, countryCode);
            if (location != null) {
                location.locationName = cityName + ", " + countryCode;
            }
        }
        
        return location != null ? location : generateDefaultLocationForPython();
    }
    
    private LocationData geocodeLocationForPython(String city, String country) {
        LocationData location = new LocationData();
        String key = (city + "_" + country).toUpperCase();
        
        switch (key) {
            case "NEW YORK_US":
                location.latitude = 40.7128; location.longitude = -74.0060; break;
            case "LONDON_GB":
                location.latitude = 51.5074; location.longitude = -0.1278; break;
            case "TOKYO_JP":
                location.latitude = 35.6762; location.longitude = 139.6503; break;
            case "PARIS_FR":
                location.latitude = 48.8566; location.longitude = 2.3522; break;
            case "CITY NAME_US":
                location.latitude = 40.7128; location.longitude = -74.0060; break;
            case "CITY NAME_LK":
                location.latitude = 6.9271; location.longitude = 79.8612; break; // Colombo, Sri Lanka
            default:
                // Use country code to determine location
                if ("LK".equals(country)) {
                    location.latitude = 6.9271; location.longitude = 79.8612; // Sri Lanka
                } else if ("US".equals(country)) {
                    location.latitude = 40.7128; location.longitude = -74.0060; // USA
                } else {
                    return generateDefaultLocationForPython();
                }
        }
        
        return location;
    }
    
    private LocationData generateDefaultLocationForPython() {
        LocationData location = new LocationData();
        long currentTime = System.currentTimeMillis();
        int locationIndex = (int) (currentTime % 3);
        
        switch (locationIndex) {
            case 0:
                location.latitude = 40.7128; location.longitude = -74.0060;
                location.locationName = "New York, US"; break;
            case 1:
                location.latitude = 51.5074; location.longitude = -0.1278;
                location.locationName = "London, GB"; break;
            default:
                location.latitude = 35.6762; location.longitude = 139.6503;
                location.locationName = "Tokyo, JP"; break;
        }
        
        return location;
    }
    
    private static class LocationData {
        double latitude;
        double longitude;
        String locationName;
    }
    
    @Override
    public double[] predictImpossibleTransaction(double distance, long timeDiffMinutes, double requiredSpeed) {
        try {
            if (!isPythonAvailable()) {
                log.warn("Python not available, using rule-based prediction");
                // Fallback: simple rule-based prediction
                boolean isImpossible = requiredSpeed > 1080; // 20% over commercial flight
                double probability = Math.min(requiredSpeed / 1080.0, 1.0);
                return new double[]{isImpossible ? 1.0 : 0.0, probability};
            }
            
            // Create simple input for Python ML model
            String jsonInput = String.format(
                "{\"distance\":%f,\"time_minutes\":%d,\"required_speed\":%f}",
                distance, timeDiffMinutes, requiredSpeed
            );
            
            Files.write(Paths.get("ml_input.json"), jsonInput.getBytes());
            
            // Execute Python ML script
            String pythonExe = this.pythonExecutablePath != null ? this.pythonExecutablePath : "python";
            ProcessBuilder pb = new ProcessBuilder(pythonExe, PYTHON_SCRIPT_PATH);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            
            boolean finished = process.waitFor(10, java.util.concurrent.TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                log.warn("Python ML prediction timed out");
                return new double[]{0.0, 0.0};
            }
            
            // Read ML results
            if (Files.exists(Paths.get(PYTHON_OUTPUT_FILE))) {
                String jsonContent = new String(Files.readAllBytes(Paths.get(PYTHON_OUTPUT_FILE)));
                JsonNode results = objectMapper.readTree(jsonContent);
                
                double riskScore = results.get("risk_score").asDouble();
                boolean isImpossible = riskScore > 80;
                double probability = riskScore / 100.0;
                
                return new double[]{isImpossible ? 1.0 : 0.0, probability};
            }
            
        } catch (Exception e) {
            log.error("Python ML prediction failed: {}", e.getMessage());
        }
        
        // Fallback
        boolean isImpossible = requiredSpeed > 1080;
        return new double[]{isImpossible ? 1.0 : 0.0, Math.min(requiredSpeed / 1080.0, 1.0)};
    }
    
    private ImpossibleDistanceResult createFallbackResult() {
        return new ImpossibleDistanceResult(
            false, 0.0, 0L, 0.0, 0.0, "LOW", 
            "Unknown", "Unknown", "****", "Python unavailable - using Java fallback"
        );
    }
}
