package com.moroccantube.app.mapper;

import com.moroccantube.app.dto.CommentDto;
import com.moroccantube.app.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentDto toDto(Comment comment);
    Comment toEntity(CommentDto dto);

}
