package net.com.fms_core.controller;

import lombok.RequiredArgsConstructor;
import net.com.fms_core.dto.AIToggleDTO;
import net.com.fms_core.service.impl.AIToggleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai-toggle")
@RequiredArgsConstructor
public class AIToggleController {
    
    private final AIToggleService aiToggleService;
    
    @GetMapping
    public ResponseEntity<AIToggleDTO> getAIToggleStatus() {
        boolean enabled = aiToggleService.isAIRulesEnabled();
        return ResponseEntity.ok(new AIToggleDTO(enabled));
    }
    
    @PostMapping
    public ResponseEntity<AIToggleDTO> updateAIToggleStatus(@RequestBody AIToggleDTO toggleDTO) {
        aiToggleService.setAIRulesEnabled(toggleDTO.getUseAIRules(), "admin");
        return ResponseEntity.ok(toggleDTO);
    }
}