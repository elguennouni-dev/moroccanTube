package com.moroccantube.elguennouni_dev.mapper;

import com.moroccantube.elguennouni_dev.dto.VideoDto;
import com.moroccantube.elguennouni_dev.model.Video;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VideoMapper {

    VideoDto toDto(Video video);
    Video toEntity(VideoDto videoDto);

}
