package com.moroccantube.app.mapper;

import com.moroccantube.app.dto.UserDto;
import com.moroccantube.app.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto userDto);
}