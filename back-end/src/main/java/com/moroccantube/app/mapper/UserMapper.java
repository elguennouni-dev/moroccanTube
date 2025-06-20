package com.moroccantube.app.mapper;

import com.moroccantube.app.dto.UserDto;
import com.moroccantube.app.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    UserDto toDto(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    User toEntity(UserDto userDto);
}