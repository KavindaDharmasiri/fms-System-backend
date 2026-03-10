/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.service.impl;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.dto.*;
import net.com.fms_core.entity.PaymentNetwork;
import net.com.fms_core.repository.PaymentNetworkRepository;
import net.com.fms_core.service.PaymentNetworkService;
import net.com.fms_core.util.MessageConstant;
import net.com.fms_core.util.mapping.PageMapper;
import net.com.fms_core.util.mapping.PaymentNetworkMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentNetworkServiceIMPL implements PaymentNetworkService {
    private final PaymentNetworkRepository paymentNetworkRepository;
    private final PaymentNetworkMapper paymentNetworkMapper;
    @Override
    public ResponseEntity addPaymentNetwork(AddPaymentNetworkDTO addPaymentNetworkDTO) {
        try {
            if(addPaymentNetworkDTO.getPaymentNetworkId() > 0){
                return updatePaymentNetwork(addPaymentNetworkDTO);
            }else {
                PaymentNetwork paymentNetwork = new PaymentNetwork(
                        addPaymentNetworkDTO.getNetworkName(),
                        addPaymentNetworkDTO.getBin(),
                        addPaymentNetworkDTO.getBinLength(),
                        addPaymentNetworkDTO.isStatus()? "ACTIVE":"INACTIVE",
                        "Admin",
                        "Admin"
                );
                paymentNetworkRepository.save(paymentNetwork);
                ApiResponseDTO<String> response = ApiResponseDTO.success(MessageConstant.MSG_PAYMENT_NETWORK_SUCCESSFULLY_ADDED);
                log.info("::::Payment Network Added - {} - {}",paymentNetwork.getPaymentNetworkId(),addPaymentNetworkDTO.getNetworkName());
                return new ResponseEntity(ApiResponseDTO.success(response), HttpStatus.CREATED);
            }
        }catch (Exception e){
            throw new InternalServerErrorException("Error occurred in add Payment Network.");
        }
    }
    private ResponseEntity updatePaymentNetwork(AddPaymentNetworkDTO addPaymentNetworkDTO){
        try {
            Optional<PaymentNetwork> byId = paymentNetworkRepository.findById(addPaymentNetworkDTO.getPaymentNetworkId());
            if (byId.isPresent()){
                PaymentNetwork paymentNetwork = byId.get();
                paymentNetwork.setNetworkName(addPaymentNetworkDTO.getNetworkName());
                paymentNetwork.setBin(addPaymentNetworkDTO.getBin());
                paymentNetwork.setBinLength(addPaymentNetworkDTO.getBinLength());
                paymentNetwork.setStatus(addPaymentNetworkDTO.isStatus()? "ACTIVE":"INACTIVE");
                paymentNetwork.setCreatedBy("Admin");
                paymentNetwork.setUpdatedBy("Admin");
                paymentNetworkRepository.save(paymentNetwork);
                ApiResponseDTO<String> response = ApiResponseDTO.success(MessageConstant.MSG_PAYMENT_NETWORK_SUCCESSFULLY_UPDATED);
                log.info("::::Payment Network Updated - {} - {}",paymentNetwork.getPaymentNetworkId(),addPaymentNetworkDTO.getNetworkName());
                return new ResponseEntity(ApiResponseDTO.success(response), HttpStatus.OK);
            }else {
                throw new NotFoundException("Payment Network not founded.");
            }
        }catch (Exception e){
            throw new InternalServerErrorException("Error occurred in update Payment Network.");
        }
    }
    @Override
    public ResponseEntity listPaymentNetwork(ListPaymentNetworkRequestDTO listPaymentNetworkRequestDTO) {
        try {
            log.error(""+listPaymentNetworkRequestDTO.getPageNo());
            log.error(""+listPaymentNetworkRequestDTO.getPageSize());
            Pageable pageable = PageRequest.of(listPaymentNetworkRequestDTO.getPageNo(), listPaymentNetworkRequestDTO.getPageSize());
            Page<PaymentNetwork> paymentNetworkPage = paymentNetworkRepository.listPaymentNetworks(
                    listPaymentNetworkRequestDTO.getPaymentNetworkID(),
                    listPaymentNetworkRequestDTO.getPaymentNetworkName(),
                    listPaymentNetworkRequestDTO.getBin(),
                    listPaymentNetworkRequestDTO.getBinLength(),
                    listPaymentNetworkRequestDTO.getStatus(),
                    pageable);
            List<PaymentNetworkReturnDTO> paymentNetworkDTOList = paymentNetworkMapper.toPaymentNetworkDTOList(paymentNetworkPage.getContent());
            log.info("::::Returned All Payment Networks.");
            return new ResponseEntity(PageMapper.mapPage(paymentNetworkPage,paymentNetworkDTOList,pageable),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            throw new InternalServerErrorException("Error occurred in list payment networks.");
        }
    }
    @Override
    public ResponseEntity getPaymentNetworkByID(int paymentNetworkID) {
        try {
            Optional<PaymentNetwork> byId = paymentNetworkRepository.findById(paymentNetworkID);
            if (byId.isPresent()){
                PaymentNetworkReturnDTO paymentNetworkDTO = paymentNetworkMapper.toPaymentNetworkDTO(byId.get());
                log.info("::::Returned Payment Network - {} - {}.",byId.get().getPaymentNetworkId(),byId.get().getNetworkName());
                return new ResponseEntity(ApiResponseDTO.success(paymentNetworkDTO), HttpStatus.CREATED);
            }else {
                throw new NotFoundException("Payment Network not founded.");
            }
        }
        catch (NotFoundException e){
            throw e;
        }
        catch (Exception e){
            log.error(e.getMessage());
            throw new InternalServerErrorException("Error occurred in get payment network.");
        }
    }
    
    @Override
    public ResponseEntity deletePaymentNetwork(int paymentNetworkID) {
        try {
            Optional<PaymentNetwork> byId = paymentNetworkRepository.findById(paymentNetworkID);
            if (byId.isPresent()){
                paymentNetworkRepository.deleteById(paymentNetworkID);
                log.info("::::Payment Network Deleted - {} - {}.", paymentNetworkID, byId.get().getNetworkName());
                return new ResponseEntity(ApiResponseDTO.success("Payment Network deleted successfully"), HttpStatus.OK);
            }else {
                throw new NotFoundException("Payment Network not found.");
            }
        }
        catch (NotFoundException e){
            throw e;
        }
        catch (Exception e){
            log.error(e.getMessage());
            throw new InternalServerErrorException("Error occurred in delete payment network.");
        }
    }
}
