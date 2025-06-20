package com.moroccantube.app.mapper;

import com.moroccantube.app.dto.CommentDto;
import com.moroccantube.app.dto.VideoDto;
import com.moroccantube.app.dto.VideoSummaryDto;
import com.moroccantube.app.model.Comment;
import com.moroccantube.app.model.Video;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface VideoMapper {

//    @Mapping(target = "uploaderUsername", expression = "java(video.getUser() != null ? video.getUser().getUsername() : null)")
    // @Mapping(target = "comments", source = "comments", qualifiedByName = "mapCommentsToDto")
//    @Mapping(target = "likesCount", expression = "java(video.getLikes() != null ? video.getLikes().size() : 0)")
//    @Mapping(target = "commentsCount", expression = "java(video.getComments() != null ? video.getComments().size() : 0)")

    @Mapping(target = "uploaderUsername", expression = "java(video.getUser() != null ? video.getUser().getUsername() : null)")
    VideoDto toDto(Video video);


    @Mapping(target = "username", expression = "java(video.getUser() != null ? video.getUser().getUsername() : null)")
    VideoSummaryDto SUMMARY_DTO(Video video);

}
