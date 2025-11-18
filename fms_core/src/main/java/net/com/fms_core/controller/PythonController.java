package net.com.fms_core.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.dto.ApiResponseDTO;
import net.com.fms_core.service.PythonIntegrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/python")
public class PythonController {
    
    private final PythonIntegrationService pythonService;
    
    @GetMapping("/status")
    public ResponseEntity<ApiResponseDTO> getPythonStatus() {
        boolean available = pythonService.isPythonAvailable();
        
        ApiResponseDTO response = new ApiResponseDTO();
        response.setSuccess(true);
        response.setMessage(available ? "Python is available" : "Python is not available");
        response.setData(available);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/test-analysis")
    public ResponseEntity<ApiResponseDTO> testPythonAnalysis() {
        try {
            // Create test transaction
            net.com.fms_core.dto.message.IsoMessageDTO testTransaction = 
                new net.com.fms_core.dto.message.IsoMessageDTO();
            testTransaction.setPan("1234567890123456");
            testTransaction.setAmount(1000.0);
            
            // Run Python analysis
            var result = pythonService.callPythonAnalysis(testTransaction);
            
            ApiResponseDTO response = new ApiResponseDTO();
            response.setSuccess(true);
            response.setMessage("Python analysis completed");
            response.setData(result);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Python test failed: {}", e.getMessage());
            
            ApiResponseDTO response = new ApiResponseDTO();
            response.setSuccess(false);
            response.setMessage("Python test failed: " + e.getMessage());
            
            return ResponseEntity.ok(response);
        }
    }
}