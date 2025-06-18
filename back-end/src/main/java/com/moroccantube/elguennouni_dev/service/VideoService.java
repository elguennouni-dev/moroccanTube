package com.moroccantube.elguennouni_dev.service;

import com.moroccantube.elguennouni_dev.dto.VideoDto;
import com.moroccantube.elguennouni_dev.mapper.VideoMapper;
import com.moroccantube.elguennouni_dev.model.Video;
import com.moroccantube.elguennouni_dev.repository.VideoRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
public class VideoService {

    @Autowired private VideoRepository videoRepository;
    @Autowired private VideoMapper videoMapper;

    public ResponseEntity<?> getAllVideosByUserID(Long userId) {
        List<Video> videos = videoRepository.findAllByUserId(userId);

        if (videos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No videos found for this user ID: " + userId));
        }

        List<VideoDto> videoDtoList = videos.stream()
                .map(videoMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(videoDtoList);
    }


    public ResponseEntity<?> getVideoById(Long videoId) {
        Optional<Video> videoOptional = videoRepository.findById(videoId);
        if (videoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message","No video found with this ID: " + videoId));
        }

        VideoDto videoDto = videoMapper.toDto(videoOptional.get());

        return ResponseEntity.ok(videoDto);
    }

    // Search by video title
    public ResponseEntity<?> searchByTitle(String searchText) {
        List<Video> videoList = videoRepository.findByTitleContaining(searchText);
        if (videoList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message","No videos found contains this tile: " + searchText));
        }

        List<VideoDto> videoDtos = videoList.stream().map(videoMapper::toDto).toList();
        return ResponseEntity.ok(videoDtos);
    }

}
