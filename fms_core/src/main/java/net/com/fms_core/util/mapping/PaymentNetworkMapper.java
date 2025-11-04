/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.util.mapping;
import net.com.fms_core.dto.PaymentNetworkDTO;
import net.com.fms_core.dto.PaymentNetworkReturnDTO;
import net.com.fms_core.entity.PaymentNetwork;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;
@Mapper(componentModel = "spring")
public interface PaymentNetworkMapper {
    PaymentNetworkMapper INSTANCE = Mappers.getMapper(PaymentNetworkMapper.class);
    PaymentNetworkReturnDTO toPaymentNetworkDTO(PaymentNetwork paymentNetwork);
    List<PaymentNetworkReturnDTO> toPaymentNetworkDTOList(List<PaymentNetwork> paymentNetworks);
}
