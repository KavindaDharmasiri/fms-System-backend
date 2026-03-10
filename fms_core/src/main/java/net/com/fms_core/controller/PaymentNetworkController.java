/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.dto.AddPaymentNetworkDTO;
import net.com.fms_core.dto.ListPaymentNetworkRequestDTO;
import net.com.fms_core.service.PaymentNetworkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment-network")
public class PaymentNetworkController {
    private final PaymentNetworkService paymentNetworkService;
    @PostMapping("/add-payment-network")
    public ResponseEntity addPaymentNetwork(@RequestBody AddPaymentNetworkDTO addPaymentNetworkDTO){
        return paymentNetworkService.addPaymentNetwork(addPaymentNetworkDTO);
    }
    @PostMapping("/list-payment-networks")
    public ResponseEntity listPaymentNetwork(@RequestBody ListPaymentNetworkRequestDTO listPaymentNetworkRequestDTO){
        return  paymentNetworkService.listPaymentNetwork(listPaymentNetworkRequestDTO);
    }
    @PostMapping("/get-payment-network-by-id")
    public ResponseEntity  getPaymentNetworkByID(@RequestParam("paymentNetworkID")int paymentNetworkID){
        return paymentNetworkService.getPaymentNetworkByID(paymentNetworkID);
    }
    
    @DeleteMapping("/delete-payment-network/{id}")
    public ResponseEntity deletePaymentNetwork(@PathVariable("id") int paymentNetworkID){
        return paymentNetworkService.deletePaymentNetwork(paymentNetworkID);
    }
}
