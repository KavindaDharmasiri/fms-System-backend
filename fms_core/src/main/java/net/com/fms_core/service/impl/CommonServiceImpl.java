/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.service.impl;
import lombok.RequiredArgsConstructor;
import net.com.fms_core.dto.ApiResponseDTO;
import net.com.fms_core.dto.FmsElementDTO;
import net.com.fms_core.dto.ErrorDetailDTO;
import net.com.fms_core.dto.PaymentNetworkDTO;
import net.com.fms_core.entity.FmsElement;
import net.com.fms_core.entity.FieldDependencies;
import net.com.fms_core.entity.PaymentNetwork;
import net.com.fms_core.enums.ErrorCode;
import net.com.fms_core.repository.FmsElementRepository;
import net.com.fms_core.repository.FieldDependenciesRepository;
import net.com.fms_core.repository.PaymentNetworkRepository;
import net.com.fms_core.service.CommonService;
import net.com.fms_core.service.FieldConfiguratorService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service("commonService")
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {
    private final PaymentNetworkRepository paymentNetworkRepository;
    private final ModelMapper modelMapper;
    @Override
    public ResponseEntity<ApiResponseDTO> getPaymentNetworks() {
        try {
            List<PaymentNetwork> all = paymentNetworkRepository.findAllByStatus("ACTIVE");
            List<PaymentNetworkDTO> paymentNetworkDTOS = all.stream()
                    .map(paymentNetwork -> {
                        modelMapper.typeMap(PaymentNetwork.class, PaymentNetworkDTO.class)
                                .addMappings(mapper -> {mapper.skip(PaymentNetworkDTO::setFmsRuleCollection);
                                    mapper.skip(PaymentNetworkDTO::setRuleGroupCollection);}); // Example: Skip a field
                        return modelMapper.map(paymentNetwork, PaymentNetworkDTO.class);
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.success(paymentNetworkDTOS));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponseDTO.error(ErrorDetailDTO.builder().code(ErrorCode.INTERNAL_SERVER_ERROR).message("An unexpected error occurred.").field("test").build()));
        }
    }
}
