/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.dto.ApiResponseDTO;
import net.com.fms_core.dto.FmsElementDTO;
import net.com.fms_core.dto.ErrorDetailDTO;
import net.com.fms_core.dto.FilterDto;
import net.com.fms_core.enums.ErrorCode;
import net.com.fms_core.service.FieldConfiguratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/field-configurator")
public class FieldConfiguratorController {
    private final FieldConfiguratorService fieldConfiguratorService;
    @PostMapping("/save-configurator")
    public ResponseEntity<ApiResponseDTO> saveFieldConfigurator(@RequestBody FmsElementDTO FmsElementDTO) {
        return fieldConfiguratorService.saveConfigurator(FmsElementDTO);
    }
    @PostMapping("/get-table-data")
    public ResponseEntity<ApiResponseDTO> getTableData(@RequestBody FilterDto filterDto, @RequestParam int page, @RequestParam int size) {
        return fieldConfiguratorService.getTableData(page, size,filterDto);
    }
    @GetMapping("/get-all-by-status")
    public ResponseEntity<ApiResponseDTO> getByStatus() {
        return fieldConfiguratorService.getByStatus();
    }
    @GetMapping("/get-field-configurator")
    public ResponseEntity<ApiResponseDTO> getSingleConfigurator(@RequestParam int configId) {
        return fieldConfiguratorService.getSingleConfigurator(configId);
    }
    @DeleteMapping("/delete-field-dependencies")
    public ResponseEntity<ApiResponseDTO> deleteFieldDependencie(@RequestParam int depId) {
        return fieldConfiguratorService.deleteFieldDependencie(depId);
    }
    @DeleteMapping("/delete-fms-element")
    public ResponseEntity<ApiResponseDTO> deletfmsElement(@RequestParam int elemntId) {
        return fieldConfiguratorService.deletfmsElement(elemntId);
    }
}
