package com.micro.authentication.mapper;

import com.micro.authentication.dto.SignUpRq;
import com.micro.authentication.dto.UserDto;
import com.micro.authentication.entity.Role;
import com.micro.authentication.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import static org.mapstruct.factory.Mappers.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserMapper INSTANCE = getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", source = "role")
    @Mapping(target = "password", source = "password")
    User toEntity(SignUpRq signUpRq, Role role, String password);

    @Mapping(source = "user.id", target = "id")
    @Mapping(source = "user.email", target = "login")
    @Mapping(source = "token", target = "token")
    UserDto toUserDto(User user, String token);
}
