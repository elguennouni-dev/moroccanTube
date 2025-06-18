package com.moroccantube.elguennouni_dev.mapper;

import com.moroccantube.elguennouni_dev.dto.UserDto;
import com.moroccantube.elguennouni_dev.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto userDto);
}