/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.service;
import net.com.fms_core.dto.AddPaymentNetworkDTO;
import net.com.fms_core.dto.ListPaymentNetworkRequestDTO;
import org.springframework.http.ResponseEntity;
public interface PaymentNetworkService {
    ResponseEntity addPaymentNetwork(AddPaymentNetworkDTO addPaymentNetworkDTO);
    ResponseEntity listPaymentNetwork(ListPaymentNetworkRequestDTO listPaymentNetworkRequestDTO);
    ResponseEntity getPaymentNetworkByID(int paymentNetworkID);
}
