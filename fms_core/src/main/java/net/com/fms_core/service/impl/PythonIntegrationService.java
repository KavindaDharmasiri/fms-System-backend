package net.com.fms_core.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */

@Slf4j
@Service
public class PythonIntegrationService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public double[] predictImpossibleTransaction(double distance, long timeDiffMinutes, double requiredSpeedKmh) {
        try {
            // Prepare input JSON
            String inputJson = String.format(
                    "{\"distance_km\": %.2f, \"time_diff_minutes\": %d, \"required_speed_kmh\": %.2f}",
                    distance, timeDiffMinutes, requiredSpeedKmh
            );

            // Call Python script
            ProcessBuilder pb = new ProcessBuilder(
                    "python", "C:\\Users\\kavinda_d\\Documents\\e soft\\final project\\project\\fms backend\\fms_core\\src\\main\\java\\net\\com\\fms_core\\script\\predict.py",
                    inputJson
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Read Python output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
            process.waitFor();

            // Parse JSON result
            String jsonOutput = output.toString();
            Map<String, Object> result = objectMapper.readValue(jsonOutput, Map.class);
            int prediction = (Integer) result.get("prediction");
            double probability = (Double) result.get("probability");

            return new double[]{prediction, probability};

        } catch (Exception e) {
            log.error("Python model call failed: {}", e.getMessage());
            return new double[]{0, 0.0};
        }
    }
}
