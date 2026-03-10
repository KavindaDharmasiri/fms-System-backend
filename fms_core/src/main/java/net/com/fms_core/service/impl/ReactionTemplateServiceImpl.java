/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.service.impl;
import lombok.RequiredArgsConstructor;
import net.com.fms_core.dto.*;
import net.com.fms_core.entity.ReactionTemplate;
import net.com.fms_core.entity.ReactionTemplateRole;
import net.com.fms_core.enums.ErrorCode;
import net.com.fms_core.repository.ReactionTemplateRepository;
import net.com.fms_core.repository.ReactionTemplateRoleRepository;
import net.com.fms_core.repository.RoleRepository;
import net.com.fms_core.service.ReactionTemplateService;
import net.com.fms_core.templateSpecification.ReactionTemplateSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("reactionTemplateService")
@RequiredArgsConstructor
public class ReactionTemplateServiceImpl implements ReactionTemplateService {
    private final ReactionTemplateRepository reactionTemplateRepository;
    private final RoleRepository roleRepository;
    private final ReactionTemplateRoleRepository reactionTemplateRoleRepository;
    private final ModelMapper modelMapper;
    @Override
    public ResponseEntity<ApiResponseDTO> getAllReactionTemplates(FilterDto filter, int page, int size) {
        try{
            Pageable pageable = PageRequest.of(page, size, Sort.by("reactionTemplateId").ascending());
            Page<ReactionTemplate> all = reactionTemplateRepository.findAll(ReactionTemplateSpecification.filterBy(filter), pageable);
            List<ReactionTemplateDTO> reactionTemplateDTOS = all.getContent().stream()
                    .map(reactionTemplate -> {
                        modelMapper.typeMap(ReactionTemplate.class, ReactionTemplateDTO.class)
                                .addMappings(mapper -> {mapper.skip(ReactionTemplateDTO::setReactionTemplateRoleCollection);
                                    mapper.skip(ReactionTemplateDTO::setRuleGroupCollection);
                                    }); // Example: Skip a field
                        return modelMapper.map(reactionTemplate, ReactionTemplateDTO.class);
                    })
                    .collect(Collectors.toList());
            PaginatedResponseDTO<ReactionTemplateDTO> paginatedResponse = new PaginatedResponseDTO<>(
                    reactionTemplateDTOS,
                    all.getTotalElements(),
                    all.getTotalPages(),
                    all.getNumber(),
                    all.getSize()
            );
            return ResponseEntity.ok(ApiResponseDTO.success(paginatedResponse));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponseDTO.error(ErrorDetailDTO.builder().code(ErrorCode.INTERNAL_SERVER_ERROR).message("An unexpected error occurred.").field("").build()));
        }
    }
    @Override
    public ResponseEntity<ApiResponseDTO> getAllReactionTemplateList() {
        try{
            List<ReactionTemplate> all = reactionTemplateRepository.findAll();
            List<ReactionTemplateDTO> reactionTemplateDTOS = all.stream()
                    .map(reactionTemplate -> {
                        modelMapper.typeMap(ReactionTemplate.class, ReactionTemplateDTO.class)
                                .addMappings(mapper -> {mapper.skip(ReactionTemplateDTO::setReactionTemplateRoleCollection);
                                    mapper.skip(ReactionTemplateDTO::setRuleGroupCollection);
                                });
                        return modelMapper.map(reactionTemplate, ReactionTemplateDTO.class);
                    })
                    .collect(Collectors.toList());
            List<ReactionTemplateDTO> paginatedResponse = reactionTemplateDTOS;
            return ResponseEntity.ok(ApiResponseDTO.success(paginatedResponse));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponseDTO.error(ErrorDetailDTO.builder().code(ErrorCode.INTERNAL_SERVER_ERROR).message("An unexpected error occurred.").field("").build()));
        }
    }
    @Override
    @Transactional
    public ResponseEntity<ApiResponseDTO> saveReactionTemplate(ReactionTemplateDTO reactionTemplateDTO) {
        try {
            if (reactionTemplateDTO.getReactionTemplateId() != null && reactionTemplateDTO.getReactionTemplateId() != 0) {
                ReactionTemplate existingTemplate = reactionTemplateRepository.findById(reactionTemplateDTO.getReactionTemplateId())
                        .orElseThrow(() -> new RuntimeException("Reaction Template not found"));
                modelMapper.map(reactionTemplateDTO, existingTemplate);
                existingTemplate.setStatus(reactionTemplateDTO.getStatus().equals("true")?"ACTIVE":"INACTIVE" );
                reactionTemplateRepository.save(existingTemplate);
                reactionTemplateRoleRepository.deleteByReactionTemplateId(existingTemplate);
                reactionTemplateDTO.getRoleids().forEach(roleId -> {
                    ReactionTemplateRole reactionTemplateRole = new ReactionTemplateRole();
                    reactionTemplateRole.setStatus("ACTIVE");
                    reactionTemplateRole.setCreatedBy(reactionTemplateDTO.getUser());
                    reactionTemplateRole.setUpdatedBy(reactionTemplateDTO.getUser());
                    reactionTemplateRole.setReactionTemplateId(existingTemplate);
                    reactionTemplateRole.setRoleId(roleRepository.findById(roleId).get());
                    reactionTemplateRoleRepository.save(reactionTemplateRole);
                });
                return ResponseEntity.ok(ApiResponseDTO.success(modelMapper.map(existingTemplate, ReactionTemplateDTO.class)));
            } else {
                ReactionTemplate newTemplate = modelMapper.map(reactionTemplateDTO, ReactionTemplate.class);
                newTemplate.setReactionTemplateId(null);
                newTemplate.setStatus(reactionTemplateDTO.getStatus().equals("true")?"ACTIVE":"INACTIVE" );
                ReactionTemplate save = reactionTemplateRepository.save(newTemplate);
                if(reactionTemplateDTO.getRoleids() != null)
                {
                    reactionTemplateDTO.getRoleids().forEach(roleId -> {
                        ReactionTemplateRole reactionTemplateRole = new ReactionTemplateRole();
                        reactionTemplateRole.setStatus("ACTIVE");
                        reactionTemplateRole.setCreatedBy(reactionTemplateDTO.getUser());
                        reactionTemplateRole.setUpdatedBy(reactionTemplateDTO.getUser());
                        reactionTemplateRole.setReactionTemplateId(save);
                        reactionTemplateRole.setRoleId(roleRepository.findById(roleId).get());
                        reactionTemplateRoleRepository.save(reactionTemplateRole);
                    });
                }
                return ResponseEntity.ok(ApiResponseDTO.success(modelMapper.map(save, ReactionTemplateDTO.class)));
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponseDTO.error(ErrorDetailDTO.builder().code(ErrorCode.INTERNAL_SERVER_ERROR).message("An unexpected error occurred.").field("").build()));
        }
    }
    @Override
    public ResponseEntity<ApiResponseDTO> getAllReactionTemplateById(int temId) {
        try {
            Optional<ReactionTemplate> byId = reactionTemplateRepository.findById(temId);
            ReactionTemplateDTO reactionTemplateDTO = new ReactionTemplateDTO();
            reactionTemplateDTO.setReactionTemplateId(byId.get().getReactionTemplateId());
            reactionTemplateDTO.setTemplateUuid(byId.get().getTemplateUuid());
            reactionTemplateDTO.setTemplateName(byId.get().getTemplateName());
            reactionTemplateDTO.setSubject(byId.get().getSubject());
            reactionTemplateDTO.setStatus(byId.get().getStatus().equals("ACTIVE")?"true":"false");
            reactionTemplateDTO.setSmsEnabled(byId.get().getSmsEnabled());
            reactionTemplateDTO.setSmsBody(byId.get().getSmsBody());
            reactionTemplateDTO.setEmailEnabled(byId.get().getEmailEnabled());
            reactionTemplateDTO.setEmailBody(byId.get().getEmailBody());
            reactionTemplateDTO.setIncludedFlaggedRules(byId.get().getIncludedFlaggedRules());
            reactionTemplateDTO.setFrmEnabled(byId.get().getFrmEnabled());
            reactionTemplateDTO.setFrmBody(byId.get().getFrmBody());
            reactionTemplateDTO.setRoleids(byId.get().getReactionTemplateRoleCollection().stream()
                    .map(reactionTemplateRole -> reactionTemplateRole.getRoleId().getRoleId())
                    .collect(Collectors.toList()));
            return ResponseEntity.ok(ApiResponseDTO.success(reactionTemplateDTO));
        }catch (Exception e){
            e.printStackTrace();
            if(e.getMessage().equals("No value present")){
                return ResponseEntity.status(404).body(ApiResponseDTO.error(ErrorDetailDTO.builder().code(ErrorCode.ENTITY_NOT_FOUND).message(e.getMessage()).field("").build()));
            }
            return ResponseEntity.status(500).body(ApiResponseDTO.error(ErrorDetailDTO.builder().code(ErrorCode.INTERNAL_SERVER_ERROR).message("An unexpected error occurred.").field("").build())); 
        }
    }
    @Override
    public ResponseEntity<ApiResponseDTO> deleteReactionTemplateById(int temId) {
        try {
            Optional<ReactionTemplate> byId = reactionTemplateRepository.findById(temId);
            reactionTemplateRepository.delete(byId.get());
            return ResponseEntity.ok(ApiResponseDTO.success("Deleted"));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponseDTO.error(ErrorDetailDTO.builder().code(ErrorCode.INTERNAL_SERVER_ERROR).message("An unexpected error occurred.").field("").build()));
        }
    }
    @Override
    public ResponseEntity<ApiResponseDTO> getAllReactionTemplateByFilter(FilterDto filterDto) {
        try {
            return ResponseEntity.status(500).body(ApiResponseDTO.error(ErrorDetailDTO.builder().code(ErrorCode.INTERNAL_SERVER_ERROR).message("An unexpected error occurred.").field("").build()));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponseDTO.error(ErrorDetailDTO.builder().code(ErrorCode.INTERNAL_SERVER_ERROR).message("An unexpected error occurred.").field("").build()));
        }
    }
}
