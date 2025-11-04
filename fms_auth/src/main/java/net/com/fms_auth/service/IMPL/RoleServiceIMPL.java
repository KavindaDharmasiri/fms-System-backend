/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.service.IMPL;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_auth.dto.*;
import net.com.fms_auth.dto.common.GetAllRolesRequestDTO;
import net.com.fms_auth.dto.common.StandardResponse;
import net.com.fms_auth.entity.Privilege;
import net.com.fms_auth.entity.Role;
import net.com.fms_auth.entity.RolePrivilege;
import net.com.fms_auth.enums.Status;
import net.com.fms_auth.exceptions.AlreadyReportedException;
import net.com.fms_auth.exceptions.InternalServerErrorException;
import net.com.fms_auth.repository.PrivilegeRepository;
import net.com.fms_auth.repository.RolePrivilegeRepository;
import net.com.fms_auth.repository.RoleRepository;
import net.com.fms_auth.service.RoleService;
import net.com.fms_auth.util.AuthorizedUserContext;
import net.com.fms_auth.util.MessageConstant;
import net.com.fms_auth.util.mappers.PageMapper;
import net.com.fms_auth.util.mappers.RoleMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceIMPL implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final PrivilegeRepository privilegeRepository;
    private final RolePrivilegeRepository rolePrivilegeRepository;
    @Override
    public ResponseEntity addRole(RoleDTO roleDTO) {
        try {
            if(roleDTO.getRoleId() > 0){
                return updateRole(roleDTO);
            }
            Optional<Role> roleOptional = roleRepository.findSameRoleNameOrCode(roleDTO.getRoleName());
            if(roleOptional.isPresent()){
                throw new AlreadyReportedException(MessageConstant.MSG_ROLE_NAME_OR_ROLE_CODE_ALREADY_REPORTED);
            }
            Role role = new Role();
            role.setRoleName(roleDTO.getRoleName());
            role.setRoleCode("ROLE_"+roleDTO.getRoleName().toUpperCase());
            role.setStatus(roleDTO.getStatus());
            role.setCreatedBy(AuthorizedUserContext.getUser());
            role.setUpdatedBy(AuthorizedUserContext.getUser());
            roleRepository.save(role);
            log.info("::::New Role Added" + "("+role.getRoleName() +"-"+role.getRoleCode()+")");
            return new ResponseEntity(new StandardResponse(HttpStatus.CREATED.value(),"New User Role - "+role.getRoleCode() +" Successfully Added.",null),HttpStatus.CREATED);
        }
        catch (AlreadyReportedException alreadyReportedException){
            throw  alreadyReportedException;
        }
        catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }
    private ResponseEntity updateRole(RoleDTO roleDTO) {
        try {
            Optional<Role> byId = roleRepository.findById(roleDTO.getRoleId());
            if(byId.isPresent()){
                byId.get().setRoleName(roleDTO.getRoleName());
                byId.get().setRoleCode("ROLE_"+roleDTO.getRoleName().toUpperCase());
                byId.get().setStatus(roleDTO.getStatus());
                byId.get().setUpdatedBy(AuthorizedUserContext.getUser());
                roleRepository.save(byId.get());
                log.info("::::Role Updated." + "("+byId.get().getRoleName() +"-"+byId.get().getRoleCode()+")");
                return new ResponseEntity(new StandardResponse(200,"User Role - "+byId.get().getRoleCode() +" Edited Successfully.",null),HttpStatus.OK);
            }else {
                throw new UsernameNotFoundException("Role not founded.");
            }
        }catch (Exception e){
            log.error(e.getMessage());
            throw new InternalServerErrorException("Error occurred in update role.");
        }
    }
    @Override
    public ResponseEntity getAllRoles(GetAllRolesRequestDTO getAllRolesRequestDTO) {
        try {
            Sort.Direction direction = Sort.Direction.DESC;
            String sortBy = "createdAt";
            Sort sort = Sort.by(direction,sortBy);
            Pageable pageable = PageRequest.of(getAllRolesRequestDTO.getPage(), getAllRolesRequestDTO.getSize(),sort);
            Page<Role> rolePage = roleRepository.filterRoles(getAllRolesRequestDTO.getRoleName(),getAllRolesRequestDTO.getRoleCode(),getAllRolesRequestDTO.getStatus(),pageable);
            List<RoleDTO> roleDTOList = RoleMapper.INSTANCE.toRoleDTOList(rolePage.getContent());
            log.info("::::Returned All Roles.");
            return new ResponseEntity(PageMapper.mapPage(rolePage, roleDTOList, pageable),HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new InternalServerErrorException("Error occurred in get all roles.");
        }
    }
    @Override
    public ResponseEntity getRollByID(int roleID) {
        try {
            Optional<Role> byId = roleRepository.findById(roleID);
            if(byId.isPresent()){
                log.info("::::Returned Role by role ID - "+roleID);
                return new ResponseEntity(new StandardResponse(200,"Role by ID",roleMapper.toRoleDTO(byId.get())),HttpStatus.OK);
            }else {
                throw new UsernameNotFoundException("Role not founded.");
            }
        }catch (Exception e){
            throw new InternalServerErrorException("Error occurred in get role.");
        }
    }
    @Override
    public ResponseEntity deleteRoleByID(int roleID) {
        try {
            Optional<Role> byId = roleRepository.findById(roleID);
            if(byId.isPresent()){
                byId.get().setStatus(Status.SUSPENDED.toString());
                byId.get().setUpdatedBy(AuthorizedUserContext.getUser());
                roleRepository.save(byId.get());
                log.info("::::Role Deleted by role ID - "+roleID);
                return new ResponseEntity(new StandardResponse(200,"Role deleted successfully.",null),HttpStatus.OK);
            }else {
                throw new UsernameNotFoundException("Role not founded.");
            }
        }catch (Exception e){
            log.error(e.getMessage());
            throw new InternalServerErrorException("Error occurred in delete role.");
        }
    }
    @Override
    public ResponseEntity getPrivilegesByRoleID(int roleID) {
        try {
            List<Privilege> allPrivileges = privilegeRepository.findAll()
            .stream()
                    .sorted(Comparator
                            .comparing(Privilege::getPage)
                            .thenComparing(Privilege::getSection)
                            .thenComparing(Privilege::getPrivilegeName))
                    .toList();
            Set<Integer> rolePrivilegeIds = rolePrivilegeRepository
                    .findByRoleRoleId(roleID).stream()
                    .map(rpd -> rpd.getPrivilegeId().getPrivilegeId())
                    .collect(Collectors.toSet());
            Map<String, Map<String, List<Privilege>>> grouped =
                    allPrivileges.stream()
                            .collect(Collectors.groupingBy(
                                    Privilege::getPage,
                                    LinkedHashMap::new,
                                    Collectors.groupingBy(
                                            Privilege::getSection,
                                            LinkedHashMap::new,
                                            Collectors.toList())
                            ));
            List<PageDTO> pages = new ArrayList<>();
            List<String> sortedPages = new ArrayList<>(grouped.keySet());
            Collections.sort(sortedPages);
            for (String pageCode : sortedPages) {
                Map<String, List<Privilege>> sectionMap = grouped.get(pageCode);
                List<SectionDTO> sectionDtos = new ArrayList<>();
                List<String> sortedSections = new ArrayList<>(sectionMap.keySet());
                Collections.sort(sortedSections);
                for (String sectionCode : sortedSections) {
                    List<Privilege> privilegeList = sectionMap.get(sectionCode);
                    List<TaskDTO> privilegeDtos = privilegeList.stream()
                            .sorted(Comparator.comparing(Privilege::getPrivilegeName, Comparator.nullsLast(String::compareTo)))
                            .map(priv -> {
                                TaskDTO dto = new TaskDTO();
                                dto.setPermissionID(priv.getPrivilegeId());
                                dto.setPermissionCode(priv.getPrivilegeName());
                                dto.setPermissionStatus(rolePrivilegeIds.contains(priv.getPrivilegeId()));
                                return dto;
                            }).collect(Collectors.toList());
                    SectionDTO sectionDto = new SectionDTO();
                    sectionDto.setSectionCode(sectionCode);
                    sectionDto.setPrivileges(privilegeDtos);
                    sectionDtos.add(sectionDto);
                }
                PageDTO pageDto = new PageDTO();
                pageDto.setPageCode(pageCode);
                pageDto.setSectionDtoList(sectionDtos);
                pages.add(pageDto);
            }
            PrivilegesDTO privilegesDTO = new PrivilegesDTO(roleID,pages);
            log.info("::::Returned Role Permissions by role ID - "+roleID);
            return new ResponseEntity(privilegesDTO,HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new InternalServerErrorException("Error occurred in get privileges.");
        }
    }
    @Override
    public ResponseEntity updateRolePrivileges(PrivilegesDTO privilegesDTO) {
        try {
            Role role = roleRepository.findById(privilegesDTO.getRoleID())
                    .orElseThrow(() -> new UsernameNotFoundException("Role not found."));
            rolePrivilegeRepository.deleteByRoleRoleId(privilegesDTO.getRoleID());
            List<RolePrivilege> newMappings = new ArrayList<>();
            for (PageDTO page : privilegesDTO.getPageDTOList()) {
                for (SectionDTO section : page.getSectionDtoList()) {
                    for (TaskDTO task : section.getPrivileges()) {
                        if (task.isPermissionStatus()) {
                            Privilege privilege = privilegeRepository.findById(task.getPermissionID())
                                    .orElseThrow(() -> new UsernameNotFoundException("Privilege not found for code: " + task.getPermissionCode()));
                            RolePrivilege rpd = new RolePrivilege();
                            rpd.setRoleId(role);
                            rpd.setPrivilegeId(privilege);
                            rpd.setStatus(Status.ACTIVE.toString());
                            newMappings.add(rpd);
                        }
                    }
                }
            }
            rolePrivilegeRepository.saveAll(newMappings);
            log.info("::::Updated Role Permissions by role ID - "+privilegesDTO.getRoleID());
            return new ResponseEntity(new StandardResponse(true,"Privilege Updated Successfully"),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            throw new InternalServerErrorException("Error occurred in update privileges.");
        }
    }
    @Override
    public ResponseEntity getAllRoleNames() {
        List<Role> roles = roleRepository.findAll();
        List<RoleDTO> roleDTOList = roles.stream()
                .sorted(Comparator.comparing(Role::getRoleName))
                .map(role -> {
            RoleDTO dto = new RoleDTO();
            dto.setRoleId(role.getRoleId());
            dto.setRoleName(role.getRoleName());
            return dto;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(roleDTOList, HttpStatus.OK);
    }
    @Override
    public ResponseEntity<List<String>>  getAllPageNames() {
        List<String> pages = privilegeRepository.findDistinctPages();
        return ResponseEntity.ok(pages);
    }
    @Override
    public ResponseEntity<List<String>> getAllSectionNames() {
        List<String> sections = privilegeRepository.findDistinctSections();
        return ResponseEntity.ok(sections);
    }
    @Override
    public ResponseEntity<List<String>> getAllTaskNames() {
        List<String> tasks = privilegeRepository.findDistinctTasks();
        return ResponseEntity.ok(tasks);
    }
}
