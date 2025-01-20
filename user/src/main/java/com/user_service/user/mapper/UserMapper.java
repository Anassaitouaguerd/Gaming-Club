package com.user_service.user.mapper;

import com.user_service.user.dto.user.UserDTO;
import com.user_service.user.entity.Role;
import com.user_service.user.entity.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import javax.xml.transform.Source;
import java.lang.annotation.Target;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "role", source = "role", qualifiedByName = "roleToString")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    UserDTO toDTO(User user);

    @InheritInverseConfiguration
    @Mapping(target = "role", source = "role", qualifiedByName = "stringToRole")
    User toEntity(UserDTO dto);

    @Named("roleToString")
    default String roleToString(Role role) {
        return role != null ? role.getName() : null;
    }

    @Named("stringToRole")
    default Role stringToRole(String roleName) {
        return roleName != null ? Role.builder().name(roleName).build() : null;
    }

}