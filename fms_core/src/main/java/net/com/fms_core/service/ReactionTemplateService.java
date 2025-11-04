/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.service;
import net.com.fms_core.dto.ApiResponseDTO;
import net.com.fms_core.dto.FilterDto;
import net.com.fms_core.dto.ReactionTemplateDTO;
import org.springframework.http.ResponseEntity;

public interface ReactionTemplateService {
    ResponseEntity<ApiResponseDTO> getAllReactionTemplates(FilterDto filterDto, int page, int size);
    ResponseEntity<ApiResponseDTO> getAllReactionTemplateList();
    ResponseEntity<ApiResponseDTO> saveReactionTemplate(ReactionTemplateDTO reactionTemplateDTO);
    ResponseEntity<ApiResponseDTO> getAllReactionTemplateById(int temId);
    ResponseEntity<ApiResponseDTO> deleteReactionTemplateById(int temId);
    ResponseEntity<ApiResponseDTO> getAllReactionTemplateByFilter(FilterDto filterDto);
}
