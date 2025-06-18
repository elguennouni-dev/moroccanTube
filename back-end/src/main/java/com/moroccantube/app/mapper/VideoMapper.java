package com.moroccantube.app.mapper;

import com.moroccantube.app.dto.VideoDto;
import com.moroccantube.app.model.Video;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VideoMapper {

    VideoDto toDto(Video video);
    Video toEntity(VideoDto videoDto);

}
