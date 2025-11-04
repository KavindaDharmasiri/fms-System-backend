/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.service.impl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.dto.ApiPageReqDTO;
import net.com.fms_core.dto.ValidateTransactionDTO;
import net.com.fms_core.dto.ValidationResultDTO;
import net.com.fms_core.dto.message.*;
import net.com.fms_core.entity.FmsElement;
import net.com.fms_core.entity.FieldDependencies;
import net.com.fms_core.entity.ValidateTransaction;
import net.com.fms_core.repository.FmsElementRepository;
import net.com.fms_core.repository.FieldDependenciesRepository;
import net.com.fms_core.repository.ValidateTransactionRepository;
import net.com.fms_core.service.ValidationService;
import net.com.fms_core.util.ApiCommonMethod;
import net.com.fms_core.util.ApiConstants;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service("validationService")
public class ValidationServiceIMPL implements ValidationService {
    private final FmsElementRepository FmsElementRepository;
    private final FieldDependenciesRepository fieldDependenciesRepository;
    private final ValidateTransactionRepository validateTransactionRepository;
    private Map<String, FmsElement> FmsElementMap;
    private Map<String, List<FieldDependencies>> FmsFieldDependencyMap;
    private ValidationResultDTO validationResultDTO;
    @Override
    public Page<ValidateTransactionDTO> filterTransactions(ValidateTransactionDTO dto, ApiPageReqDTO pageable) {
        try{
            Page<ValidateTransaction> validateTransactions = validateTransactionRepository.filterTransactions(
                    dto.getValidateTransactionId(),
                    dto.getIsValid(),
                    dto.getErrorMessage(),
                    dto.getIsoMessage(),
                    dto.getFromTransactionTime(),
                    dto.getToTransactionTime(),
                    ApiCommonMethod.pageableFrom(pageable, "validateTransactionId")
            );
            return validateTransactions.map(validateTransaction -> {
                ValidateTransactionDTO validateTransactionDTO = new ValidateTransactionDTO();
                validateTransactionDTO.setValidateTransactionId(validateTransaction.getValidateTransactionId());
                validateTransactionDTO.setIsValid(validateTransaction.getIsValid());
                validateTransactionDTO.setErrorMessage(validateTransaction.getErrorMessage());
                validateTransactionDTO.setIsoMessage(validateTransaction.getIsoMessage());
                validateTransactionDTO.setTransactionTime(validateTransaction.getTransactionTime());
                return validateTransactionDTO;
            });
        }
        catch (Exception e){
            log.error("Failed to get transactions: {}", e.getMessage(), e);
            return null;
        }
    }
    @Override
    public ValidationResultDTO ValidateTransaction(IsoMessageDTO isoMessageDTO) {
        validationResultDTO = new ValidationResultDTO();
        validationResultDTO.setValid(true);
        if (isoMessageDTO == null) {
            validationResultDTO.setValid(false);
            validationResultDTO.setErrorMessages(List.of("Transaction packet is null"));
            return validationResultDTO;
        }
        List<FmsElement> FmsElements = FmsElementRepository.findAll();
        FmsElementMap = FmsElements.stream()
                .collect(Collectors.toMap(
                        FmsElement::getVariableName,
                        Function.identity(),
                        (existing, replacement) -> existing,
                        HashMap::new
                ));
        List<FieldDependencies> fieldDependencies = fieldDependenciesRepository.findAll();
        FmsFieldDependencyMap = fieldDependencies.stream()
                .collect(Collectors.groupingBy(
                        dependency -> dependency.getFmsElementId().getVariableName(),
                        Collectors.toList()
                ));
        validateAllFields(isoMessageDTO);
        log.info("Validation result: {}", validationResultDTO);
        saveTransaction(isoMessageDTO);
        return validationResultDTO;
    }
    private void saveTransaction(IsoMessageDTO isoMessageDTO) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            String isoMessageJSON = objectMapper.writeValueAsString(isoMessageDTO);
            String errorMessagesJSON = objectMapper.writeValueAsString(validationResultDTO.getErrorMessages());
            ValidateTransaction validateTransaction = new ValidateTransaction();
            validateTransaction.setIsoMessage(isoMessageJSON);
            validateTransaction.setErrorMessage(errorMessagesJSON);
            validateTransaction.setIsValid(validationResultDTO.isValid());
            validateTransaction.setTransactionTime(new java.sql.Timestamp(System.currentTimeMillis()));
            validateTransactionRepository.save(validateTransaction);
        }
        catch (Exception e){
            log.error("Failed to save transaction: {}", e.getMessage(), e);
        }
    }
    public void validateAllFields(IsoMessageDTO isoMessageDTO) {
        try {
            for (Field field : IsoMessageDTO.class.getDeclaredFields()) {
                Class<?> type = field.getType();
                if ((type == String.class || type == Integer.class || type == int.class ||
                        type == Double.class || type == double.class ||
                        type.getSimpleName().startsWith("VisaField")) &&
                        !Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true);
                    Object value = field.get(isoMessageDTO);
                    if (value == null) continue;
                    String fieldName = field.getName();
                    String errorMessage = fieldName + " is invalid";
                    if (value instanceof String || value instanceof Number) {
                        validateField(String.valueOf(value), fieldName, errorMessage);
                        validateFieldDependency(isoMessageDTO, String.valueOf(value), fieldName);
                    } else {
                        log.warn("Non-primitive field detected: {}. Add custom validation if needed.", fieldName);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Validation failed", e);
        }
    }
    private void validateField(String value, String fieldName, String errorMessage) {
        if (value != null) {
            FmsElement FmsElement = FmsElementMap.get(fieldName);
            if (FmsElement != null) {
                boolean matches = value.matches(FmsElement.getValidation());
                if (!matches) {
                    if (FmsElement.getValidationMessage() != null) {
                        errorMessage = FmsElement.getValidationMessage();
                    }
                    setValidationResultError(errorMessage);
                }
            }
        }
    }
    private void validateFieldDependency(IsoMessageDTO isoMessageDTO, String value, String fieldName) throws JsonProcessingException, NoSuchFieldException, IllegalAccessException {
        if (value != null) {
            String errorMessage;
            FmsElement FmsElement = FmsElementMap.get(fieldName);
            List<FieldDependencies> fieldDependencies = FmsFieldDependencyMap.get(fieldName);
            if (FmsElement != null && fieldDependencies != null) {
                for (FieldDependencies fieldDependency : fieldDependencies) {
                    FmsElement depFmsElement = FmsElementMap.get(fieldDependency.getDepElementId().getVariableName());
                    if (depFmsElement == null) {
                        errorMessage = "Field " + fieldName + ": The dependent field " + fieldDependency.getDepElementId().getVariableName() + " does not exist";
                        setValidationResultError(errorMessage);
                    }
                    if (fieldDependency.getMainOperator().equals(ApiConstants.EQUAL)
                            && value.equals(fieldDependency.getValue())) {
                        dependentFieldValidate(isoMessageDTO, FmsElement, depFmsElement, fieldDependency);
                    } else if (fieldDependency.getMainOperator().equals(ApiConstants.NOT_EQUAL)
                            && !value.equals(fieldDependency.getValue())) {
                        dependentFieldValidate(isoMessageDTO, FmsElement, depFmsElement, fieldDependency);
                    } else if (fieldDependency.getMainOperator().equals(ApiConstants.GREATER_THAN)
                            && Double.parseDouble(value) > Double.parseDouble(fieldDependency.getValue())) {
                        dependentFieldValidate(isoMessageDTO, FmsElement, depFmsElement, fieldDependency);
                    } else if (fieldDependency.getMainOperator().equals(ApiConstants.LESS_THAN)
                            && Double.parseDouble(value) < Double.parseDouble(fieldDependency.getValue())) {
                        dependentFieldValidate(isoMessageDTO, FmsElement, depFmsElement, fieldDependency);
                    } else if (fieldDependency.getMainOperator().equals(ApiConstants.GREATER_THAN_OR_EQUAL)
                            && Double.parseDouble(value) >= Double.parseDouble(fieldDependency.getValue())) {
                        dependentFieldValidate(isoMessageDTO, FmsElement, depFmsElement, fieldDependency);
                    } else if (fieldDependency.getMainOperator().equals(ApiConstants.LESS_THAN_OR_EQUAL)
                            && Double.parseDouble(value) <= Double.parseDouble(fieldDependency.getValue())) {
                        dependentFieldValidate(isoMessageDTO, FmsElement, depFmsElement, fieldDependency);
                    } else if (fieldDependency.getMainOperator().equals(ApiConstants.TRUE)
                            && (value != null && !value.isEmpty())) {
                        dependentFieldValidate(isoMessageDTO, FmsElement, depFmsElement, fieldDependency);
                    } else if (fieldDependency.getMainOperator().equals(ApiConstants.FALSE)
                            && (value == null || value.isEmpty())) {
                        dependentFieldValidate(isoMessageDTO, FmsElement, depFmsElement, fieldDependency);
                    }
                }
            }
        }
    }
    private void dependentFieldValidate(IsoMessageDTO isoMessageDTO, FmsElement FmsElement, FmsElement depFmsElement, FieldDependencies fieldDependency ) throws JsonProcessingException, NoSuchFieldException, IllegalAccessException {
        String errorMessage;
        if (depFmsElement == null) {
            errorMessage = "Field " + FmsElement.getVariableName() + ": The dependent field " + fieldDependency.getDepElementId().getVariableName() + " does not exist";
            setValidationResultError(errorMessage);
        }
        ObjectMapper mapper = new ObjectMapper();
        List<String> depFmsElementValues = mapper.readValue(depFmsElement.getValue(), new TypeReference<List<String>>() {});
        Field field = IsoMessageDTO.class.getDeclaredField(depFmsElement.getVariableName());
        field.setAccessible(true);
        Object valueISOField = field.get(isoMessageDTO);
        String valueISOFieldAsString = (valueISOField != null) ? valueISOField.toString() : "";
        if ((fieldDependency.getDepOperator().equals(ApiConstants.EQUAL)
                && !fieldDependency.getDepValue().equals(valueISOFieldAsString))
        ) {
            errorMessage = "Field " + FmsElement.getVariableName() + ": The value of dependent field " + depFmsElement.getVariableName() + " is not equal to " + fieldDependency.getDepValue();
            setValidationResultError(errorMessage);
        } else if (fieldDependency.getDepOperator().equals(ApiConstants.NOT_EQUAL)
                && fieldDependency.getDepValue().equals(valueISOFieldAsString)) {
            errorMessage = "Field " + FmsElement.getVariableName() + ": The value of dependent field " + depFmsElement.getVariableName() + " is equal to " + fieldDependency.getDepValue();
            setValidationResultError(errorMessage);
        } else if (fieldDependency.getDepOperator().equals(ApiConstants.GREATER_THAN)
                && !(Double.parseDouble(fieldDependency.getDepValue()) < Double.parseDouble(valueISOFieldAsString))) {
            errorMessage = "Field " + FmsElement.getVariableName() + ": The value of dependent field " + depFmsElement.getVariableName() + " is not greater than " + fieldDependency.getDepValue();
            System.out.println("errorMessage = " + errorMessage);
            setValidationResultError(errorMessage);
        } else if (fieldDependency.getDepOperator().equals(ApiConstants.LESS_THAN)
                && !(Double.parseDouble(fieldDependency.getDepValue()) > Double.parseDouble(valueISOFieldAsString))) {
            errorMessage = "Field " + FmsElement.getVariableName() + ": The value of dependent field " + depFmsElement.getVariableName() + " is not less than " + fieldDependency.getDepValue();
            setValidationResultError(errorMessage);
        } else if (fieldDependency.getDepOperator().equals(ApiConstants.GREATER_THAN_OR_EQUAL)
                && !(Double.parseDouble(fieldDependency.getDepValue()) <= Double.parseDouble(valueISOFieldAsString))) {
            errorMessage = "Field " + FmsElement.getVariableName() + ": The value of dependent field " + depFmsElement.getVariableName() + " is not greater than or equal " + fieldDependency.getDepValue();
            setValidationResultError(errorMessage);
        } else if (fieldDependency.getDepOperator().equals(ApiConstants.LESS_THAN_OR_EQUAL)
                && !(Double.parseDouble(fieldDependency.getDepValue()) >= Double.parseDouble(valueISOFieldAsString))) {
            errorMessage = "Field " + FmsElement.getVariableName() + ": The value of dependent field " + depFmsElement.getVariableName() + " is not less than or equal " + fieldDependency.getDepValue();
            setValidationResultError(errorMessage);
        } else if (fieldDependency.getDepOperator().equals(ApiConstants.TRUE)
                && (valueISOFieldAsString == null || valueISOFieldAsString.isEmpty())) {
            errorMessage = "Field " + FmsElement.getVariableName() + ": The value of dependent field " + depFmsElement.getVariableName() + " is not present";
            setValidationResultError(errorMessage);
        } else if (fieldDependency.getDepOperator().equals(ApiConstants.FALSE)
                && (valueISOFieldAsString != null && !valueISOFieldAsString.isEmpty())) {
            errorMessage = "Field " + FmsElement.getVariableName() + ": The value of dependent field " + depFmsElement.getVariableName() + " is present";
            setValidationResultError(errorMessage);
        }
    }
    public void setValidationResultError(String errorMessage){
        validationResultDTO.setValid(false);
        if (validationResultDTO.getErrorMessages() == null) {
            validationResultDTO.setErrorMessages(new ArrayList<>(List.of(errorMessage)));
        } else {
            validationResultDTO.getErrorMessages().add(errorMessage);
        }
    }
}
