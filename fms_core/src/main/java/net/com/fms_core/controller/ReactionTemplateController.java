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
import net.com.fms_core.dto.FilterDto;
import net.com.fms_core.dto.ReactionTemplateDTO;
import net.com.fms_core.service.ReactionTemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reaction-template")
public class ReactionTemplateController {
    private final ReactionTemplateService reactionTemplateService;
    @PostMapping("/get-reaction-templates")
    public ResponseEntity<ApiResponseDTO> getAllReactionTemplates(@RequestBody FilterDto filterDto,@RequestParam int page, @RequestParam int size) {
        return reactionTemplateService.getAllReactionTemplates(filterDto,page,size);
    }
    @GetMapping("/get-all-reaction-templates")
    public ResponseEntity<ApiResponseDTO> getAllReactionTemplateList() {
        return reactionTemplateService.getAllReactionTemplateList();
    }
    @PostMapping("/save-reaction-templates")
    public ResponseEntity<ApiResponseDTO> saveReactionTemplate(@RequestBody ReactionTemplateDTO reactionTemplateDTO) {
        return reactionTemplateService.saveReactionTemplate(reactionTemplateDTO);
    }
    @GetMapping("/get-reaction-template-byid")
    public ResponseEntity<ApiResponseDTO> getAllReactionTemplateById(@RequestParam int temId) {
        return reactionTemplateService.getAllReactionTemplateById(temId);
    }
    @DeleteMapping("/delete-reaction-template-byid")
    public ResponseEntity<ApiResponseDTO> deleteReactionTemplateById(@RequestParam int temId) {
        return reactionTemplateService.deleteReactionTemplateById(temId);
    }
    @GetMapping("/get-reaction-template-by-filter")
    public ResponseEntity<ApiResponseDTO> getAllReactionTemplateByFilter(@RequestBody FilterDto filterDto) {
        return reactionTemplateService.getAllReactionTemplateByFilter(filterDto);
    }
}
