/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.service;
import net.com.fms_core.dto.ApiResponseDTO;
import net.com.fms_core.dto.FmsElementDTO;
import net.com.fms_core.dto.FilterDto;
import org.springframework.http.ResponseEntity;

public interface FieldConfiguratorService {
    ResponseEntity<ApiResponseDTO> saveConfigurator(FmsElementDTO FmsElementDTO);
    ResponseEntity<ApiResponseDTO> getTableData(int page, int size, FilterDto filterDto);
    ResponseEntity<ApiResponseDTO> getSingleConfigurator(int configId);
    ResponseEntity<ApiResponseDTO> getByStatus();
    ResponseEntity<ApiResponseDTO> deleteFieldDependencie(int depId);
    ResponseEntity<ApiResponseDTO> deletfmsElement(int elemntId);
}
