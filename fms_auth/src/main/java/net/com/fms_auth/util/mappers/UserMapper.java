/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.util.mappers;
import net.com.fms_auth.dto.request.SystemUserDto;
import net.com.fms_auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE= Mappers.getMapper(UserMapper.class);
    SystemUserDto mapToSystemUserDto(User user);
    List<SystemUserDto> toSystemUserDto(List<User> users);
}
