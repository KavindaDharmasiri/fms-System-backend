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
import net.com.fms_core.dto.message.IsoMessageDTO;
import net.com.fms_core.entity.TransactionHistory;
import net.com.fms_core.service.TransactionService;
import net.com.fms_core.service.impl.script.FutureRuleGenerationService;
import net.com.fms_core.service.impl.script.TransactionExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import java.util.List;
@RestController
@RequestMapping("/api/v1/tran")
public class TransactionController {
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

    @GetMapping("/generate-future-rules")
    public String generateFutureRules() {
        String csvPath = "C:\\Users\\kavinda_d\\Documents\\e soft\\final project\\project\\fms backend\\fms_core\\src\\main\\java\\net\\com\\fms_core\\script\\rulegenerator\\transactions.csv";
        exportService.exportTransactionsToCSV(csvPath);
        String generatedRules = ruleService.generateRules(csvPath);
        return generatedRules;
    }
    
    @GetMapping("/deploy-ai-rules")
    public String deployAIRules() {
        String csvPath = "C:\\Users\\kavinda_d\\Documents\\e soft\\final project\\project\\fms backend\\fms_core\\src\\main\\java\\net\\com\\fms_core\\script\\rulegenerator\\transactions.csv";
        String generatedRules = ruleService.generateRules(csvPath);
        deploymentService.saveAndDeployRules(generatedRules);
        return "AI rules deployed successfully";
    }
}
