/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.service.impl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.dto.*;
import net.com.fms_core.entity.DualAuthentication;
import net.com.fms_core.repository.DualAuthenticationRepository;
import net.com.fms_core.service.DualAuthenticationService;
import net.com.fms_core.util.AuthorizedUserContext;
import net.com.fms_core.util.MessageConstant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DualAuthenticationServiceIMPL implements DualAuthenticationService {
    private final DualAuthenticationRepository dualAuthenticationRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public void compareJson(String json1, String json2,String lastModifiedUser,String task,Date createdDate,String configuration) throws Exception {
        JsonNode node1 = objectMapper.readTree(json1);
        JsonNode node2 = objectMapper.readTree(json2);
        Iterator<String> fields = node1.fieldNames();
        while (fields.hasNext()) {
            String field = fields.next();
            JsonNode value1 = node1.get(field);
            JsonNode value2 = node2.get(field);
            UUID randomUuid = UUID.randomUUID();
            List<DualAuthentication> dualAuthenticationList = new ArrayList<>();
            if (!value1.equals(value2)) {
                DualAuthentication dualAuthentication = new DualAuthentication();
                dualAuthentication.setLastModifiedUser(lastModifiedUser);
                dualAuthentication.setNewModifiedUser(AuthorizedUserContext.getUser());
                dualAuthentication.setTask(task);
                dualAuthentication.setModifiedFeild(field);
                dualAuthentication.setOldValue(value1.textValue());
                dualAuthentication.setOldValue(value2.textValue());
                dualAuthentication.setDate(createdDate);
                dualAuthentication.setStatus("PENDING");
                dualAuthentication.setCreatedBy(AuthorizedUserContext.getUser());
                dualAuthentication.setUpdatedBy(AuthorizedUserContext.getUser());
                dualAuthentication.setGroupID(randomUuid.toString().substring(0,5));
                dualAuthentication.setConfiguration(configuration);
                dualAuthenticationList.add(dualAuthentication);
            }
            dualAuthenticationRepository.saveAll(dualAuthenticationList);
        }
    }
    @Override
    public ResponseEntity getAllPendingApproveRequests(FilterDualAuthentication filterDualAuthentication) {
        try {
            Pageable pageable = PageRequest.of(filterDualAuthentication.getPageNo(),filterDualAuthentication.getPageSize());
            log.info(filterDualAuthentication.toString());
            Page<DualAuthentication> authenticationPage = dualAuthenticationRepository.filterDualAuthentication(
                    filterDualAuthentication.getFrom(),
                    filterDualAuthentication.getTo(),
                    filterDualAuthentication.getConfiguration(),
                    filterDualAuthentication.getTask(),
                    filterDualAuthentication.getModifiedUser()
                    ,pageable);
            Map<String, List<DualAuthentication>> groupedByGroupId = authenticationPage.getContent().stream()
                    .collect(Collectors.groupingBy(DualAuthentication::getGroupID));
            List<DualAuthDTO> dtoList = groupedByGroupId.values().stream()
                    .map(groupedList -> {
                        DualAuthentication exampleRecord = groupedList.get(0);
                        DualAuthDTO dto = new DualAuthDTO();
                        dto.setIdentifier(exampleRecord.getGroupID());
                        dto.setTask(exampleRecord.getTask());
                        dto.setModifiedUser(exampleRecord.getLastModifiedUser());
                        dto.setModifiedDate(exampleRecord.getDate());
                        dto.setCretedDate(exampleRecord.getCreatedAt());
                        return dto;
                    })
                    .collect(Collectors.toList());
            PageImpl<DualAuthDTO> dualAuthDTOS = new PageImpl<>(dtoList, pageable, authenticationPage.getTotalElements());
            log.info("::::Returned All due dual authentication requests.");
            return new ResponseEntity<>(dualAuthDTOS,HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw new InternalServerErrorException("Error occur in get all pending requests.");
        }
    }
    @Override
    public ResponseEntity approveRequest(String identifier) {
        try {
            List<DualAuthentication> dualAuthenticationList = dualAuthenticationRepository.getByGroupID(identifier);
            for (DualAuthentication dualAuthentication:dualAuthenticationList){
                dualAuthentication.setStatus("APPROVED");
                dualAuthentication.setUpdatedBy(AuthorizedUserContext.getUser());
            }
            ApiResponseDTO<String> response = ApiResponseDTO.success(MessageConstant.MSG_DUAL_AUTHENTICATION_SUCCESSFULLY_APPROVED);
            log.info("::::Request Approved - {}",identifier);
            return new ResponseEntity(ApiResponseDTO.success(response), HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new InternalServerErrorException("Error occur in get approve requests.");
        }
    }
    @Override
    public ResponseEntity rejectRequest(RejectDualAuthDTO rejectDualAuthDTO) {
        try {
            List<DualAuthentication> dualAuthenticationList = dualAuthenticationRepository.getByGroupID(rejectDualAuthDTO.getIdentifier());
            for (DualAuthentication dualAuthentication:dualAuthenticationList){
                dualAuthentication.setStatus("REJECTED");
                dualAuthentication.setUpdatedBy(AuthorizedUserContext.getUser());
            }
            dualAuthenticationRepository.saveAll(dualAuthenticationList);
            ApiResponseDTO<String> response = ApiResponseDTO.success(MessageConstant.MSG_DUAL_AUTHENTICATION_SUCCESSFULLY_REJECTED);
            log.info("::::Request rejected - {}",rejectDualAuthDTO.getIdentifier());
            return new ResponseEntity(ApiResponseDTO.success(response), HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new InternalServerErrorException("Error occur in reject requests.");
        }
    }
    @Override
    public ResponseEntity getRequestByIdentifier(String identifier) {
        try {
            List<DualAuthentication> byGroupID = dualAuthenticationRepository.getByGroupID(identifier);
            DualAuthResponseDTO dualAuthResponseDTO = new DualAuthResponseDTO();
            List<ParameterDTO> parameterDTOList = new ArrayList<>();
            for (DualAuthentication dualAuthentication:byGroupID){
                ParameterDTO parameterDTO = new ParameterDTO();
                parameterDTO.setParameterName(dualAuthentication.getTask());
                parameterDTO.setOldValue(dualAuthentication.getOldValue());
                parameterDTO.setNewValue(dualAuthentication.getNewValue());
                parameterDTOList.add(parameterDTO);
                dualAuthResponseDTO.setConfiguration(dualAuthentication.getConfiguration());
                dualAuthResponseDTO.setTask(dualAuthentication.getTask());
                dualAuthResponseDTO.setLastModifiedUser(dualAuthentication.getLastModifiedUser());
                dualAuthResponseDTO.setNewModifiedUser(dualAuthentication.getNewModifiedUser());
            }
            dualAuthResponseDTO.setParameterDTOList(parameterDTOList);
            log.info("::::Request returned - {}",identifier);
            return new ResponseEntity(dualAuthResponseDTO,HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new InternalServerErrorException("Error occur in get request.");
        }
    }
}
