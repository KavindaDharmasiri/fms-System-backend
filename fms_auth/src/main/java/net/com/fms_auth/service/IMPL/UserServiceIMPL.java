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
import net.com.fms_auth.dto.RoleDTO;
import net.com.fms_auth.dto.common.StandardResponse;
import net.com.fms_auth.dto.request.SysUserFilterDTO;
import net.com.fms_auth.dto.request.SystemUserDto;
import net.com.fms_auth.dto.response.SysUserResponseDTO;
import net.com.fms_auth.dto.response.UsernameForSelectorDTO;
import net.com.fms_auth.entity.Role;
import net.com.fms_auth.entity.User;
import net.com.fms_auth.entity.UserRole;
import net.com.fms_auth.enums.Status;
import net.com.fms_auth.exceptions.AlreadyReportedException;
import net.com.fms_auth.exceptions.InternalServerErrorException;
import net.com.fms_auth.repository.RoleRepository;
import net.com.fms_auth.repository.UserRepository;
import net.com.fms_auth.repository.UserRoleRepository;
import net.com.fms_auth.service.UserService;
import net.com.fms_auth.util.AuthorizedUserContext;
import net.com.fms_auth.util.MessageConstant;
import net.com.fms_auth.util.mappers.PageMapper;
import net.com.fms_auth.util.mappers.UserMapper;
import org.mapstruct.factory.Mappers;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceIMPL implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<StandardResponse> addSystemUser(SystemUserDto systemUserDto) {
          try {
              if (userRepository.existsById(systemUserDto.getUserId())){
                  log.error("----------------------------------------");
                  log.info("update section");
                   return updateSystemUser(systemUserDto);
              }else {
                  if(userRepository.existsByUsername(systemUserDto.getUsername())){
                      throw new AlreadyReportedException(MessageConstant.SYSTEM_USER_USERNAME_ALREADY_REPORTED);
                  }else if(userRepository.existsByEmail(systemUserDto.getEmail())){
                      throw new AlreadyReportedException(MessageConstant.SYSTEM_USER_EMAIL_ALREADY_REPORTED);
                  } else if (userRepository.existsByContact(systemUserDto.getContact())) {
                      throw new AlreadyReportedException(MessageConstant.SYSTEM_USER_CONTACT_ALREADY_REPORTED);
                  } else if (userRepository.existsByNic(systemUserDto.getNic())) {
                      throw new AlreadyReportedException(MessageConstant.SYSTEM_USER_NIC_ALREADY_REPORTED);
                  } else if (userRepository.existsByEmpId(systemUserDto.getEmpId())) {
                      throw new AlreadyReportedException(MessageConstant.SYSTEM_USER_EMPLOYEE_ID_ALREADY_REPORTED);
                  }
                  String randomPassword = UUID.randomUUID().toString().replace("-", "");
                  User user=new User();
                  user.setUserUuid(UUID.randomUUID().toString());
                  user.setUserName(systemUserDto.getUsername());
                  user.setPassword(systemUserDto.getPassword());
                  user.setPassword(passwordEncoder.encode(systemUserDto.getPassword()));
                  user.setCreatedAt(new Date());
                  user.setUpdatedAt(new Date());
                  user.setCreatedBy(AuthorizedUserContext.getUser());
                  user.setUpdatedBy(AuthorizedUserContext.getUser());
                  if(systemUserDto.getStatus().toLowerCase().equals("true")){
                      user.setStatus("ACTIVE");
                  }else {
                      user.setStatus("INACTIVE");
                  }
                  user.setLoginAttempts(0);
                  user.setUserCategory(systemUserDto.getUserCategory());
                  user.setNic(systemUserDto.getNic());
                  user.setContact(systemUserDto.getContact());
                  user.setEmail(systemUserDto.getEmail());
                  user.setEmpId(systemUserDto.getEmpId());
                  user.setFullName(systemUserDto.getFullName());
                  user.setCity(systemUserDto.getCity());
                  user.setAddress(systemUserDto.getAddress());
                  User saved = userRepository.save(user);
                  System.out.println(saved);
                  System.out.println(systemUserDto.getUserRole().size());
                  for (Integer roleId:systemUserDto.getUserRole()) {
                      System.out.println(roleId);
                      UserRole userRole=new UserRole();
                      Role role = roleRepository.findById(roleId)
                              .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));
                      userRole.setRoleId(role);
                      userRole.setUserId(saved);
                      userRole.setStatus("INACTIVE");
                      userRole.setCreatedAt(new Date());
                      userRole.setUpdatedAt(new Date());
                      userRole.setCreatedBy(AuthorizedUserContext.getUser());
                      userRole.setUpdatedBy(AuthorizedUserContext.getUser());
                      userRoleRepository.save(userRole); // save each record
                  }
                  return new ResponseEntity(new StandardResponse(HttpStatus.CREATED.value(),"New System User "+saved.getUserId() +" Successfully Added.",null),HttpStatus.CREATED);
              }
          }catch (AlreadyReportedException alreadyReportedException){
              throw alreadyReportedException;
          }catch (Exception ex){
              log.error(ex.getMessage());
              return new  ResponseEntity(new StandardResponse(HttpStatus.EXPECTATION_FAILED.value(), ex.getMessage(), null ), HttpStatus.EXPECTATION_FAILED);
          }
    }
    private ResponseEntity<StandardResponse> updateSystemUser(SystemUserDto systemUserDto) {
        try {
            log.info(systemUserDto.toString());
            Optional<User> byId = userRepository.findById(systemUserDto.getUserId());
            if(byId.isPresent()){
                byId.get().setUserName(systemUserDto.getUsername());
                byId.get().setUserCategory(systemUserDto.getUserCategory());
                byId.get().setNic(systemUserDto.getNic());
                byId.get().setContact(systemUserDto.getContact());
                byId.get().setEmpId(systemUserDto.getEmpId());
                byId.get().setEmail(systemUserDto.getEmail());
                byId.get().setPassword(passwordEncoder.encode(systemUserDto.getPassword()));
                byId.get().setFullName(systemUserDto.getFullName());
                byId.get().setCity(systemUserDto.getCity());
                byId.get().setAddress(systemUserDto.getAddress());
                if(systemUserDto.getStatus().toLowerCase().equals("true")){
                    byId.get().setStatus("ACTIVE");
                }else {
                    byId.get().setStatus("INACTIVE");
                }
                byId.get().setCreatedBy(AuthorizedUserContext.getUser());
                byId.get().setUpdatedBy(AuthorizedUserContext.getUser());
                userRepository.save(byId.get());
                log.info("::::User Updated." + "("+byId.get().getUserId() +"-"+byId.get().getUserName()+")");
                List<UserRole> userRoleList = userRoleRepository.findAllByUserId(byId.get());
                for (UserRole userRole:userRoleList) {
                    for (Integer roleId:systemUserDto.getUserRole()) {
                        Role role = roleRepository.findById(roleId)
                                .orElseThrow(() -> new RuntimeException("User not found with ID: " + roleId));
                        userRole.setRoleId(role);
                        userRole.setUserId(byId.get());
                        if(systemUserDto.getStatus().toLowerCase().equals("true")){
                            userRole.setStatus("ACTIVE");
                        }else {
                            userRole.setStatus("INACTIVE");
                        }
                        userRole.setCreatedAt(new Date());
                        userRole.setUpdatedAt(new Date());
                        userRole.setCreatedBy(AuthorizedUserContext.getUser());
                        userRole.setUpdatedBy(AuthorizedUserContext.getUser());
                        userRoleRepository.save(userRole); // save each record
                    }
                }
                return new ResponseEntity(new StandardResponse(HttpStatus.CREATED.value(),"User updated successfully.",null),HttpStatus.OK);
            }else {
                throw new UsernameNotFoundException("Role not founded.");
            }
        }catch (Exception e){
            log.error(e.getMessage());
            throw new InternalServerErrorException("Error occurred in update role.");
        }
    }
    @Override
    public ResponseEntity<StandardResponse> getAllSystemUsers(SysUserFilterDTO filter) {
        try {
            Sort.Direction direction = Sort.Direction.DESC;
            String sortBy = "createdAt";
            Sort sort = Sort.by(direction,sortBy);
            Pageable pageable=PageRequest.of(filter.getPage(), filter.getSize(), sort);
                Page<User> sysUserPage = userRepository.findAllUsers(
                        filter.getUserId(),
                        filter.getUsername(),
                        filter.getFullName(),
                        filter.getNic(),
                        filter.getUserRole(),
                        filter.getEmpId(),
                        filter.getEmail(),
                        filter.getContact(),
                        filter.getStatus(),
                        pageable);
            log.error("---------------------------------------------------");
            log.error("filter data :::" + sysUserPage.getContent());
            List<SystemUserDto> systemUserDtoList = sysUserPage.getContent()
                    .stream()
                    .map(user -> SystemUserDto.builder()
                            .userId(user.getUserId())
                            .userCategory(user.getUserCategory())
                            .username(user.getUserName())
                            .nic(user.getNic())
                            .userRole(user.getUserRoleCollection() != null ? user.getUserRoleCollection()
                                    .stream()
                                    .map(userRole -> userRole.getRoleId().getRoleId())
                                    .toList() : null)
                            .contact(user.getContact())
                            .email(user.getEmail())
                            .empId(user.getEmpId())
                            .fullName(user.getFullName())
                            .city(user.getCity())
                            .address(user.getAddress())
                            .status(user.getStatus())
                            .createdBy(user.getCreatedBy())
                            .updatedBy(user.getUpdatedBy())
                            .build()
                    )
                    .toList();
            return new ResponseEntity(PageMapper.mapPage(sysUserPage,systemUserDtoList,pageable),HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
            return new  ResponseEntity(new StandardResponse(HttpStatus.EXPECTATION_FAILED.value(), ex.getMessage(), null ), HttpStatus.EXPECTATION_FAILED);
        }
    }
    @Override
    public ResponseEntity<StandardResponse> deleteUserById(Integer userId) {
        try {
            Optional<User> byId = userRepository.findById(userId);
            if (byId.isPresent()) {
                byId.get().setStatus(Status.SUSPENDED.toString());
                byId.get().setUpdatedBy(AuthorizedUserContext.getUser());
                userRepository.save(byId.get());
                log.info("::::Role Deleted by role ID - " + userId);
                return new ResponseEntity(new StandardResponse(200, "User deleted successfully.", null), HttpStatus.OK);
            } else {
                throw new UsernameNotFoundException("Role not founded.");
            }
        } catch (UsernameNotFoundException ex) {
            log.error(ex.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new InternalServerErrorException("Error occurred in delete role.");
        }
        return new ResponseEntity(new StandardResponse(HttpStatus.NOT_FOUND.value(), "User Not Found.", null), HttpStatus.NOT_FOUND);
    }
    @Override
    public ResponseEntity<StandardResponse> getUserById(Integer userId) {
                    try {
                Optional<User> byId = userRepository.findById(userId);
                if (byId.isPresent()) {
                    log.info("::::Returned User by user ID - " + userId);
                   List<SystemUserDto> list= byId.stream().map(
                            user -> SystemUserDto.builder()
                                    .userId(user.getUserId())
                                    .userCategory(user.getUserCategory())
                                    .username(user.getUsername())
                                    .nic(user.getNic())
                                    .userRole(user.getUserRoleCollection() != null ? user.getUserRoleCollection()
                                            .stream()
                                            .map(userRole -> userRole.getRoleId().getRoleId())
                                            .toList() : null)
                                    .contact(user.getContact())
                                    .email(user.getEmail())
                                    .empId(user.getEmpId())
                                    .fullName(user.getFullName())
                                    .city(user.getCity())
                                    .address(user.getAddress())
                                    .status(user.getStatus())
                                    .createdBy(user.getCreatedBy())
                                    .updatedBy(user.getUpdatedBy())
                                    .build()
                    ).toList();
                    return new ResponseEntity(new StandardResponse(HttpStatus.FOUND.value(), "User by ID",list), HttpStatus.OK);
                } else {
                    throw new UsernameNotFoundException("User not founded.");
                }
            } catch (UsernameNotFoundException exception) {
                return new ResponseEntity(new StandardResponse(HttpStatus.NOT_FOUND.value(), "User Not Found", null), HttpStatus.OK);
            } catch (Exception e) {
                throw new InternalServerErrorException("Error occurred in get role.");
            }
    }
    @Override
    public ResponseEntity getAllUsersWithUsername() {
        try {
            List<User> all = userRepository.findAll();
            List<UsernameForSelectorDTO> usernameForSelectorDTOList = new ArrayList<>();
            for (User user:all){
                UsernameForSelectorDTO usernameForSelectorDTO = new UsernameForSelectorDTO(
                        user.getFullName(), user.getUsername()
                );
                usernameForSelectorDTOList.add(usernameForSelectorDTO);
            }
            return new ResponseEntity<>(usernameForSelectorDTOList,HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new InternalServerErrorException("Error occurred in get all users.");
        }
    }
}
