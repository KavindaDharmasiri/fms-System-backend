/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.com.fms_core.dto.ApiResponseDTO;
import net.com.fms_core.dto.RiskManagement.Transaction;
import net.com.fms_core.dto.TransactionFilterDto;
import net.com.fms_core.dto.TransactionHistoryDTO;
import net.com.fms_core.dto.TransactionStatusUpdateDTO;
import net.com.fms_core.dto.message.IsoMessageDTO;
import net.com.fms_core.entity.TransactionHistory;
import net.com.fms_core.service.TransactionService;
import net.com.fms_core.service.impl.script.FutureRuleGenerationService;
import net.com.fms_core.service.impl.script.TransactionExportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/v1/tran")
public class TransactionController {
    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);
    private TransactionService tranService;
    private final Sinks.Many<TransactionHistoryDTO> sink;
    private final TransactionExportService exportService;
    private final FutureRuleGenerationService ruleService;

    @Autowired
    private ObjectMapper objectMapper;
    public TransactionController(TransactionService tranService, Sinks.Many<TransactionHistoryDTO> sink, TransactionExportService exportService, FutureRuleGenerationService ruleService) {
        this.tranService = tranService;
        this.sink = sink;
        this.exportService = exportService;
        this.ruleService = ruleService;
    }
    @PostMapping("/save-tran")
    public ResponseEntity<ApiResponseDTO> saveTran(@RequestBody List<TransactionHistoryDTO> tranDto){
        return tranService.saveTestTran(tranDto);
    }
    @GetMapping("/get-all-trans")
    public ResponseEntity<ApiResponseDTO> getAllTran(@RequestBody TransactionFilterDto transactionFilterDto){
        return tranService.getAllTran(transactionFilterDto);
    }
    @GetMapping("/get-all-tran")
    public ResponseEntity<ApiResponseDTO> getAllTrans(){
        return tranService.getAllTrans();
    }
    @GetMapping(value = "/transaction/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<TransactionHistoryDTO>> streamTransactions() {
        return sink.asFlux().map(data ->
                ServerSentEvent.builder(data).build()
        );
    }
    public void publish(TransactionHistory transaction) {
        System.out.println("emit............");
        TransactionHistoryDTO dto = new TransactionHistoryDTO();
        dto.setTransactionHistoryId(transaction.getTransactionHistoryId());
        dto.setTranUuid(transaction.getTranUuid());
        dto.setTranPacket(convertJsonToDto(transaction.getTranPacket()));
        dto.setStatus(transaction.getStatus());
        dto.setCreatedAt(transaction.getCreatedAt());
        dto.setUpdatedAt(transaction.getUpdatedAt());
        dto.setCreatedBy(transaction.getCreatedBy());
        dto.setUpdatedBy(transaction.getUpdatedBy());
        sink.tryEmitNext(dto);  // Push to connected clients
    }
    public IsoMessageDTO convertJsonToDto(String json) {
        try {
            return objectMapper.readValue(json, IsoMessageDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("/get-all-vari-names")
    public ResponseEntity<ApiResponseDTO> getAllVariableNames(){
        return tranService.getAllVariableNames();
    }


    @Autowired
    private net.com.fms_core.service.impl.script.AIRuleDeploymentService deploymentService;
    
    @Autowired
    private net.com.fms_core.service.impl.script.ProductionRuleGenerationService productionRuleService;

    @PostMapping("/train-fraud-model")
    public ResponseEntity<Map<String, Object>> trainFraudModel() {
        try {
            log.info("Training fraud detection model...");
            Map<String, Object> result = productionRuleService.trainModel();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PostMapping("/generate-future-rules")
    public ResponseEntity<Map<String, Object>> generateFutureRules(@RequestBody(required = false) Map<String, Object> params) {
        try {
            // Fast rule generation from pre-trained model
            Map<String, Object> result = productionRuleService.generateRulesFromModel();
            
            if (!(Boolean) result.getOrDefault("success", false)) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PostMapping("/deploy-ai-rules")
    public ResponseEntity<Map<String, Object>> deployAIRules() {
        try {
            String csvPath = "C:\\Users\\kavinda_d\\Documents\\e soft\\final project\\project\\fms backend\\fms_core\\src\\main\\java\\net\\com\\fms_core\\script\\rulegenerator\\csv\\transactions.csv";
            String generatedRules = ruleService.generateRules(csvPath);
            deploymentService.saveAndDeployRules(generatedRules);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "AI rules deployed successfully to AI KIE base");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @GetMapping("/deploy-ai-rules")
    public ResponseEntity<Map<String, Object>> deployAIRulesGet() {
        return deployAIRules();
    }
    
    @PutMapping("/update-status")
    public ResponseEntity<ApiResponseDTO> updateTransactionStatus(@RequestBody TransactionStatusUpdateDTO updateDTO) {
        return tranService.updateTransactionStatus(updateDTO);
    }
    
    @DeleteMapping("/delete-tran/{id}")
    public ResponseEntity<ApiResponseDTO> deleteTransaction(@PathVariable Long id) {
        return tranService.deleteTransaction(id);
    }
}
