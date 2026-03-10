/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.service.impl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.dto.*;
import net.com.fms_core.dto.RiskManagement.Transaction;
import net.com.fms_core.dto.message.IsoMessageDTO;
import net.com.fms_core.entity.TestTransaction;
import net.com.fms_core.entity.TransactionHistory;
import net.com.fms_core.enums.ErrorCode;
import net.com.fms_core.repository.TestTransactionRepository;
import net.com.fms_core.repository.TransactionRepository;
import net.com.fms_core.service.TransactionService;
import net.com.fms_core.util.mapping.IsoFieldDropdownMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private TransactionRepository tranRepo;
    private TestTransactionRepository testTranRepo;
    @Autowired
    private ObjectMapper objectMapper;
    public TransactionServiceImpl(TransactionRepository tranRepo, TestTransactionRepository testTranRepo) {
        this.tranRepo = tranRepo;
        this.testTranRepo=testTranRepo;
    }
    @Override
    public ResponseEntity<ApiResponseDTO> saveTestTran(List<TransactionHistoryDTO> tranDtoList) {
        try {
            log.error("===============================================================");
            log.info("Transaction Packet : " + tranDtoList);
            for (TransactionHistoryDTO tranDto:tranDtoList) {
                TestTransaction testTransaction=new TestTransaction();
                testTransaction.setStatus(tranDto.getStatus());
                testTransaction.setTranUuid(UUID.randomUUID().toString());
                testTransaction.setCreatedAt(new Date());
                testTransaction.setUpdatedAt(new Date());
                testTransaction.setCreatedBy("ADMIN");
                testTransaction.setUpdatedBy("ADMIN");
                TestTransaction save = testTranRepo.save(testTransaction);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResponseDTO.success("Transaction save success")
            );
        }catch (Exception ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponseDTO.error(new ErrorDetailDTO(ErrorCode.INTERNAL_SERVER_ERROR, ex.getMessage(), ex.getMessage()))
            );
        }
    }
    @Override
    public ResponseEntity<ApiResponseDTO> getAllTran(TransactionFilterDto transactionFilterDto) {
        try {
            Sort.Direction direction = Sort.Direction.DESC;
            String sortBy = "createdAt";
            Sort sort = Sort.by(direction,sortBy);
            Pageable pageable= PageRequest.of(transactionFilterDto.getPage(),transactionFilterDto.getSize(), sort);
            Page<TransactionHistory> all = tranRepo.findAll(pageable);
            List list=new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();
            for (TransactionHistory tran : all) {
                try {
                    TransactionPacketDto packet = mapper.readValue(tran.getTranPacket(), TransactionPacketDto.class);
                    if (transactionFilterDto.getCardNumber() != null &&
                            (packet.getCardNumber() == null || !packet.getCardNumber().contains(transactionFilterDto.getCardNumber()))) {
                        continue;
                    }
                    if (transactionFilterDto.getTransactionHistoryId() != null &&
                            (packet.getTranId() == null || !packet.getTranId().equals(transactionFilterDto.getTransactionHistoryId()))) {
                        continue;
                    }
                    if (transactionFilterDto.getTran_amount() != null &&
                            !transactionFilterDto.getTran_amount().equals(packet.getTran_amount())) {
                        continue;
                    }
                    if (transactionFilterDto.getAcquirerBin() != null &&
                            !transactionFilterDto.getAcquirerBin().equals(packet.getAcquirerBin())) {
                        continue;
                    }
                    if (transactionFilterDto.getCurrencyCode() != null &&
                            !transactionFilterDto.getCurrencyCode().equals(packet.getCurrencyCode())) {
                        continue;
                    }
                    if (transactionFilterDto.getMerch_name() != null &&
                            !transactionFilterDto.getMerch_name().equalsIgnoreCase(packet.getMerch_name())) {
                        continue;
                    }
                    if (transactionFilterDto.getCurrencyCode() != null &&
                            !transactionFilterDto.getCurrencyCode().equalsIgnoreCase(packet.getCurrencyCode())) {
                        continue;
                    }
                    if (transactionFilterDto.getStatus() != null &&
                            !transactionFilterDto.getStatus().equalsIgnoreCase(packet.getStatus())) {
                        continue;
                    }
                    list.add(packet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResponseDTO.success(list)
            );
        }catch (Exception ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponseDTO.error(new ErrorDetailDTO(ErrorCode.INTERNAL_SERVER_ERROR, ex.getMessage(), ex.getMessage()))
            );
        }
    }
    @Override
    public ResponseEntity<ApiResponseDTO> getAllTrans() {
        try{
            List<TransactionHistory> all = tranRepo.findAllByOrderByTransactionHistoryIdDesc();
            List<TransactionHistoryDTO> dtoList = all.stream().map(tran -> {
                TransactionHistoryDTO dto = new TransactionHistoryDTO();
                dto.setTransactionHistoryId(tran.getTransactionHistoryId());
                dto.setTranUuid(tran.getTranUuid());
                dto.setTranPacket(convertJsonToDto(tran.getTranPacket()));
                dto.setStatus(tran.getStatus());
                dto.setBlockReason(tran.getBlockReason());
                dto.setActionStatus(tran.getActionStatus());
                dto.setReactionTemplateName(tran.getReactionTemplateName());
                dto.setSmsEnabled(tran.getSmsEnabled());
                dto.setEmailEnabled(tran.getEmailEnabled());
                dto.setFrmEnabled(tran.getFrmEnabled());
                dto.setManualReviewStatus(tran.getManualReviewStatus());
                dto.setManualReviewReason(tran.getManualReviewReason());
                dto.setReviewedBy(tran.getReviewedBy());
                dto.setReviewedAt(tran.getReviewedAt());
                dto.setCreatedAt(tran.getCreatedAt());
                dto.setUpdatedAt(tran.getUpdatedAt());
                dto.setCreatedBy(tran.getCreatedBy());
                dto.setUpdatedBy(tran.getUpdatedBy());
                
                // Map flagged rules
                if (tran.getTransactionFlaggedRulesCollection() != null) {
                    dto.setTransactionFlaggedRulesCollection(
                        tran.getTransactionFlaggedRulesCollection().stream()
                            .map(flagged -> {
                                TransactionFlaggedRulesDTO flaggedDto = new TransactionFlaggedRulesDTO();
                                flaggedDto.setTransactionFlaggedRulesId(flagged.getTransactionFlaggedRulesId());
                                flaggedDto.setRuleName(flagged.getFmsRuleId() != null ? flagged.getFmsRuleId().getRuleName() : null);
                                flaggedDto.setRuleGroupName(flagged.getRuleGroupId() != null ? flagged.getRuleGroupId().getGroupName() : null);
                                flaggedDto.setRiskScore(flagged.getRiskScore());
                                flaggedDto.setFlag(flagged.getFlag());
                                return flaggedDto;
                            }).collect(Collectors.toList())
                    );
                }
                
                return dto;
            }).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResponseDTO.success(dtoList)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponseDTO.error(new ErrorDetailDTO(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage(), e.getMessage()))
            );
        }
    }
    @Override
    public ResponseEntity<ApiResponseDTO> getAllVariableNames() {
        try{
            List<Map<String, String>> result = IsoFieldDropdownMapper.getIsoFieldOptions();
            result.sort(Comparator.comparing(map -> map.get("name"), Comparator.nullsFirst(String::compareToIgnoreCase)));
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResponseDTO.success(result)
            );
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponseDTO.error(new ErrorDetailDTO(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage(), e.getMessage())));
        }
    }
    public IsoMessageDTO convertJsonToDto(String json) {
        try {
            return objectMapper.readValue(json, IsoMessageDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public ResponseEntity<ApiResponseDTO> updateTransactionStatus(TransactionStatusUpdateDTO updateDTO) {
        try {
            TransactionHistory transaction = tranRepo.findByTranUuid(updateDTO.getTransactionUuid());
            if (transaction == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseDTO.error(new ErrorDetailDTO(ErrorCode.NOT_FOUND, "Transaction not found", "Transaction not found"))
                );
            }
            
            transaction.setManualReviewStatus(updateDTO.getNewStatus());
            transaction.setManualReviewReason(updateDTO.getReason());
            transaction.setReviewedBy(updateDTO.getReviewedBy());
            transaction.setReviewedAt(new Date());
            tranRepo.save(transaction);
            
            return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponseDTO.success("Transaction status updated successfully")
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponseDTO.error(new ErrorDetailDTO(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage(), e.getMessage()))
            );
        }
    }
    
    @Override
    public ResponseEntity<ApiResponseDTO> deleteTransaction(Long transactionId) {
        try {
            if (!tranRepo.existsById(transactionId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseDTO.error(new ErrorDetailDTO(ErrorCode.NOT_FOUND, "Transaction not found", "Transaction not found"))
                );
            }
            
            tranRepo.deleteById(transactionId);
            
            return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponseDTO.success("Transaction deleted successfully")
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponseDTO.error(new ErrorDetailDTO(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage(), e.getMessage()))
            );
        }
    }
}
