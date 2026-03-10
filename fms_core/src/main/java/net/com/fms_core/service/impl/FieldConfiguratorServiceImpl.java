/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.service.impl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.dto.*;
import net.com.fms_core.entity.FmsElement;
import net.com.fms_core.entity.FieldDependencies;
import net.com.fms_core.entity.PaymentNetwork;
import net.com.fms_core.enums.ErrorCode;
import net.com.fms_core.repository.FmsElementRepository;
import net.com.fms_core.repository.FieldDependenciesRepository;
import net.com.fms_core.repository.PaymentNetworkRepository;
import net.com.fms_core.service.FieldConfiguratorService;
import net.com.fms_core.templateSpecification.FieldConfigurationSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service("fieldConfiguratorService")
@RequiredArgsConstructor
@Transactional
public class FieldConfiguratorServiceImpl implements FieldConfiguratorService {
    private final FmsElementRepository FmsElementRepository;
    private final FieldDependenciesRepository fieldDependenciesRepository;
    private final PaymentNetworkRepository paymentNetworkRepository;
    private final ModelMapper modelMapper;
    @Override
    @Transactional
    public ResponseEntity<ApiResponseDTO> saveConfigurator(FmsElementDTO FmsElementDTO) {
        try{
            // Handle both fmsElementId and efmsElementId field names
            Integer elementId = FmsElementDTO.getFmsElementId() != null ? 
                FmsElementDTO.getFmsElementId() : FmsElementDTO.getEfmsElementId();
            
            FmsElement FmsElement = new FmsElement();
            if (elementId != null && elementId != 0) {
                try {
                    FmsElement = FmsElementRepository.findById(elementId).get();
                }catch (Exception e){
                    return ResponseEntity.status(500).body(ApiResponseDTO.error(ErrorDetailDTO.builder().code(ErrorCode.INTERNAL_SERVER_ERROR).message("Id is not valid.").field("").build()));
                }
            }else {
                if (FmsElementRepository.existsByElementName(FmsElementDTO.getElementName())){
                    return ResponseEntity.status(405).body(ApiResponseDTO.error(ErrorDetailDTO.builder().code(ErrorCode.INVALID_INPUT).message("Element Name Already Exist.").field("").build()));
                }
                FmsElement = new FmsElement();
            }
            FmsElement.setElementName(FmsElementDTO.getElementName());
            FmsElement.setElementCode(FmsElementDTO.getElementCode());
            FmsElement.setValidation(FmsElementDTO.getValidation());
            FmsElement.setValidationMessage(FmsElementDTO.getValidationMessage());
            FmsElement.setRiskWeight(FmsElementDTO.getRiskWeight());
            FmsElement.setOperator(FmsElementDTO.getOperator());
            FmsElement.setValue(FmsElementDTO.getValue());
            FmsElement.setStatus(FmsElementDTO.getStatus());
            FmsElement.setCreatedBy(FmsElementDTO.getCreatedBy());
            FmsElement.setUpdatedBy(FmsElementDTO.getUpdatedBy());
            FmsElement.setDescription(FmsElementDTO.getDescription());
            FmsElement.setVariableName(FmsElementDTO.getVariableName());
            PaymentNetwork byId = paymentNetworkRepository.getById(FmsElementDTO.getPaymentNetworkId());
            FmsElement.setPaymentNetwork(byId);
            FmsElement save = FmsElementRepository.save(FmsElement);
            FmsElementDTO.getFieldDependenciesCollection().forEach(fieldDependencie -> {
                FieldDependencies fieldDependencies = new FieldDependencies();
                if (fieldDependencie.getFieldDependenciesId() != null && fieldDependencie.getFieldDependenciesId() != 0) {
                    fieldDependencies = fieldDependenciesRepository.findById(fieldDependencie.getFieldDependenciesId()).get();
                } else {
                    fieldDependencies = new FieldDependencies();
                }
                fieldDependencies.setMainOperator(fieldDependencie.getMainOperator());
                fieldDependencies.setValue(fieldDependencie.getValue());
                fieldDependencies.setDepOperator(fieldDependencie.getDepOperator());
                fieldDependencies.setDepValue(fieldDependencie.getDepValue());
                fieldDependencies.setStatus(fieldDependencie.getStatus());
                fieldDependencies.setCreatedBy(fieldDependencie.getCreatedBy());
                fieldDependencies.setUpdatedBy(fieldDependencie.getUpdatedBy());
                fieldDependencies.setDepElementId(FmsElementRepository.findById( fieldDependencie.getFmsElementId()).get());
                fieldDependencies.setFmsElementId(save);
                fieldDependenciesRepository.save(fieldDependencies);
            });
            return ResponseEntity.ok(ApiResponseDTO.success(save.getElementCode()));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponseDTO.error(ErrorDetailDTO.builder().code(ErrorCode.INTERNAL_SERVER_ERROR).message("An unexpected error occurred.").field("").build()));
        }
    }
    @Override
    public ResponseEntity<ApiResponseDTO> getTableData(int page, int size, FilterDto filterDto) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("fmsElementId").descending());
            Page<FmsElement> all = FmsElementRepository.findAll(FieldConfigurationSpecification.filterBy(filterDto),pageable);
            List<FmsElementDTO> FmsElementDTOList = all.getContent().stream()
                    .map(FmsElement -> {
                        modelMapper.typeMap(FmsElement.class, FmsElementDTO.class)
                                .addMappings(mapper -> {mapper.skip(FmsElementDTO::setFmsRuleConditionCollection);
                                    mapper.skip(FmsElementDTO::setFieldDependenciesCollection);
                                    mapper.skip(FmsElementDTO::setFieldDependenciesCollection1);}); // Example: Skip a field
                        return modelMapper.map(FmsElement, FmsElementDTO.class);
                    })
                    .collect(Collectors.toList());
            FmsElementDTOList.forEach(FmsElementDTO -> {
               FmsElementDTO.setNetworkName(paymentNetworkRepository.findById(FmsElementDTO.getPaymentNetworkId()).get().getNetworkName());
            });
            PaginatedResponseDTO<FmsElementDTO> paginatedResponse = new PaginatedResponseDTO<>(
                    FmsElementDTOList,
                    all.getTotalElements(),
                    all.getTotalPages(),
                    all.getNumber(),
                    all.getSize()
            );
            return ResponseEntity.ok(ApiResponseDTO.success(paginatedResponse));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponseDTO.error(ErrorDetailDTO.builder().code(ErrorCode.INTERNAL_SERVER_ERROR).message("An unexpected error occurred.").field("").build()));
        }
    }
    @Override
    public ResponseEntity<ApiResponseDTO> getSingleConfigurator(int configId) {
        try{
            FmsElement FmsElement = FmsElementRepository.findById(configId).orElseThrow(() -> new RuntimeException("Config ID not found"));
            FmsElementDTO FmsElementDTO = new FmsElementDTO();
            FmsElementDTO.setFmsElementId(FmsElement.getFmsElementId());
            FmsElementDTO.setElementName(FmsElement.getElementName());
            FmsElementDTO.setElementCode(FmsElement.getElementCode());
            FmsElementDTO.setValidation(FmsElement.getValidation());
            FmsElementDTO.setValidationMessage(FmsElement.getValidationMessage());
            FmsElementDTO.setRiskWeight(FmsElement.getRiskWeight());
            FmsElementDTO.setOperator(FmsElement.getOperator());
            FmsElementDTO.setValue(FmsElement.getValue());
            FmsElementDTO.setStatus(FmsElement.getStatus());
            FmsElementDTO.setCreatedBy(FmsElement.getCreatedBy());
            FmsElementDTO.setUpdatedBy(FmsElement.getUpdatedBy());
            FmsElementDTO.setDescription(FmsElement.getDescription());
            FmsElementDTO.setVariableName(FmsElement.getVariableName());
            FmsElementDTO.setPaymentNetworkId(FmsElement.getPaymentNetwork().getPaymentNetworkId());
            List<FieldDependenciesDTO> fieldDependenciesDTOS = new ArrayList<>();
            FmsElement.getFieldDependenciesCollection1().forEach(fieldDependencie -> {
                if (fieldDependencie.getStatus().equals("ACTIVE")) {
                    FieldDependenciesDTO fieldDependencies = new FieldDependenciesDTO();
                    fieldDependencies.setFieldDependenciesId(fieldDependencie.getFieldDependenciesId());
                    fieldDependencies.setMainOperator(fieldDependencie.getMainOperator());
                    fieldDependencies.setValue(fieldDependencie.getValue());
                    fieldDependencies.setDepOperator(fieldDependencie.getDepOperator());
                    fieldDependencies.setDepValue(fieldDependencie.getDepValue());
                    fieldDependencies.setStatus(fieldDependencie.getStatus());
                    fieldDependencies.setCreatedBy(fieldDependencie.getCreatedBy());
                    fieldDependencies.setUpdatedBy(fieldDependencie.getUpdatedBy());
                    FmsElementDTO FmsElementDTO1 = new FmsElementDTO();
                    FmsElementDTO1.setFmsElementId(fieldDependencie.getDepElementId().getFmsElementId());
                    FmsElementDTO1.setElementCode(fieldDependencie.getDepElementId().getElementCode());
                    FmsElementDTO1.setElementName(fieldDependencie.getDepElementId().getElementName());
                    fieldDependencies.setDepElement(FmsElementDTO1);
                    FmsElementDTO1 = new FmsElementDTO();
                    FmsElementDTO1.setFmsElementId(fieldDependencie.getFmsElementId().getFmsElementId());
                    FmsElementDTO1.setElementCode(fieldDependencie.getFmsElementId().getElementCode());
                    FmsElementDTO1.setElementName(fieldDependencie.getFmsElementId().getElementName());
                    fieldDependencies.setFmsElement(FmsElementDTO1);
                    fieldDependenciesDTOS.add(fieldDependencies);
                }
            });
            FmsElementDTO.setFieldDependenciesCollection(fieldDependenciesDTOS);
            return ResponseEntity.ok(ApiResponseDTO.success(FmsElementDTO));
        }catch (Exception e){
            e.printStackTrace();
            if(e.getMessage().equals("Config ID not found")){
                return ResponseEntity.status(404).body(ApiResponseDTO.error(ErrorDetailDTO.builder().code(ErrorCode.ENTITY_NOT_FOUND).message(e.getMessage()).field("").build()));
            }
            return ResponseEntity.status(500).body(ApiResponseDTO.error(ErrorDetailDTO.builder().code(ErrorCode.INTERNAL_SERVER_ERROR).message("An unexpected error occurred.").field("").build()));
        }
    }
    @Override
    public ResponseEntity<ApiResponseDTO> getByStatus() {
        try{
            List<FmsElement> active = FmsElementRepository.findAllByStatus("ACTIVE");
            List<FmsElementDTO> FmsElementDTOList = new ArrayList<>();
            active.forEach(FmsElement -> {
                FmsElementDTO FmsElementDTO = new FmsElementDTO();
                FmsElementDTO.setElementName(FmsElement.getElementName());
                FmsElementDTO.setFmsElementId(FmsElement.getFmsElementId());
                FmsElementDTO.setElementCode(FmsElement.getElementCode());
                FmsElementDTOList.add(FmsElementDTO);
            });
            return ResponseEntity.ok(ApiResponseDTO.success(FmsElementDTOList));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponseDTO.error(ErrorDetailDTO.builder().code(ErrorCode.INTERNAL_SERVER_ERROR).message("An unexpected error occurred.").field("").build()));
        }
    }
    @Override
    public ResponseEntity<ApiResponseDTO> deleteFieldDependencie(int depId) {
        try {
            if (fieldDependenciesRepository.existsById(depId)){
                Optional<FieldDependencies> byId = fieldDependenciesRepository.findById(depId);
                byId.get().setStatus("DELETED");
                fieldDependenciesRepository.save(byId.get());
                return ResponseEntity.ok(ApiResponseDTO.success("Field dependency deleted successfully."));
            }else {
                return ResponseEntity.status(500).body(ApiResponseDTO.error(ErrorDetailDTO.builder().code(ErrorCode.INTERNAL_SERVER_ERROR).message("Id is not valid.").field("").build()));
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponseDTO.error(ErrorDetailDTO.builder().code(ErrorCode.INTERNAL_SERVER_ERROR).message("An unexpected error occurred.").field("").build()));
        }
    }
    @Override
    public ResponseEntity<ApiResponseDTO> deletfmsElement(int elemntId) {
        try {
            if (FmsElementRepository.existsById(elemntId)){
                FmsElementRepository.deleteById(elemntId);
                return ResponseEntity.ok(ApiResponseDTO.success("FMS Element deleted successfully."));
            }else {
                return ResponseEntity.status(500).body(ApiResponseDTO.error(ErrorDetailDTO.builder().code(ErrorCode.INTERNAL_SERVER_ERROR).message("Id is not valid.").field("").build()));
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponseDTO.error(ErrorDetailDTO.builder().code(ErrorCode.INTERNAL_SERVER_ERROR).message("An unexpected error occurred.").field("").build()));
        }
    }
}
