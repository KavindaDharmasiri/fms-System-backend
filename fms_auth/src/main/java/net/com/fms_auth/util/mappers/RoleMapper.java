/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.util.mappers;
import net.com.fms_auth.dto.RoleDTO;
import net.com.fms_auth.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;
@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);
    RoleDTO toRoleDTO(Role role);
    List<RoleDTO> toRoleDTOList(List<Role> roles);
}
