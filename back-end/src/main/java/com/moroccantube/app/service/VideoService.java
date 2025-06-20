package com.moroccantube.app.service;

import com.moroccantube.app.dao.VideoUploadRequest;
import com.moroccantube.app.dto.VideoDto;
import com.moroccantube.app.dto.VideoSummaryDto;
import com.moroccantube.app.dto.VideoUploadResponse;
import com.moroccantube.app.exception.video.VideoNotFoundException;
import com.moroccantube.app.exception.video.VideoUploadErrorException;
import com.moroccantube.app.mapper.CommentMapper;
import com.moroccantube.app.mapper.VideoMapper;
import com.moroccantube.app.model.User;
import com.moroccantube.app.model.Video;
import com.moroccantube.app.repository.UserRepository;
import com.moroccantube.app.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.info.MultimediaInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VideoService {

    @Autowired private VideoRepository videoRepository;
    @Autowired private VideoMapper videoMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private StorageService storageService;
//    @Autowired private CommentMapper commentMapper;

    public ResponseEntity<?> getAll() {
        List<Video> videoList = videoRepository.findAllWithUser();

        List<VideoSummaryDto> videoSummaryDtos = videoList.stream()
                .map(videoMapper::SUMMARY_DTO)
                .toList();

        return ResponseEntity.ok(Map.of(
                "data", videoSummaryDtos,
                "message", videoSummaryDtos.isEmpty() ? "No videos available at the moment" : "Videos retrieved successfully"
        ));
    }

    public VideoUploadResponse uploadVideo(MultipartFile file, VideoUploadRequest videoUploadRequest, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        try {
            String storedFilename = storageService.store(file);

            Path rootLocation = Paths.get("uploads");
            File storedVideoFile = rootLocation.resolve(storedFilename).toFile();

            Long duration = getVideoDurationInSeconds(storedVideoFile);

            Video video = new Video();
            video.setTitle(videoUploadRequest.getTitle());
            video.setDescription(videoUploadRequest.getDescription());
            video.setFilename(storedFilename);
            video.setContentType(file.getContentType());
            video.setSize(file.getSize());
            video.setUploadDate(LocalDateTime.now());
            video.setUser(user);
            video.setDuration(duration);

            Video savedVideo = videoRepository.save(video);

            return new VideoUploadResponse(
                    savedVideo.getId(),
                    savedVideo.getTitle(),
                    "Video uploaded successfully!"
            );

        } catch (IOException e) {
            throw new VideoUploadErrorException("Failed to process video file: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new VideoUploadErrorException("Video duration calculation interrupted.", e);
        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new VideoUploadErrorException("An unexpected error occurred during upload: " + e.getMessage(), e);
        }
    }

    public ResponseEntity<?> getAllVideosByUserID(UUID userId) {
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

    @Transactional
    public ResponseEntity<?> getVideoById(UUID videoId) {
        Optional<Video> videoOptional = videoRepository.findById(videoId);
        if (videoOptional.isEmpty()) {
            throw new VideoNotFoundException("Video not found");
        }

        Video video = videoOptional.get();

        VideoDto videoDto = new VideoDto();
        videoDto.setId(video.getId());
        videoDto.setSize(video.getSize());
        videoDto.setDescription(video.getDescription());
        videoDto.setFilename(video.getFilename());
        videoDto.setComments(video.getComments());
        videoDto.setContentType(video.getContentType());
        videoDto.setTitle(video.getTitle());
        videoDto.setDuration(video.getDuration());
        videoDto.setLikesCount(video.getLikes().size());
        videoDto.setCommentsCount(video.getComments().size());
        videoDto.setUploaderUsername(video.getUser().getUsername());

        return ResponseEntity.ok(videoDto);
    }

    public ResponseEntity<?> searchByTitle(String videoTitle) {
        List<Video> videoList = videoRepository.findAllByTitle(videoTitle);

        List<VideoSummaryDto> videoSummaryDtos = videoList.stream()
                .map(videoMapper::SUMMARY_DTO)
                .toList();

        return ResponseEntity.ok(Map.of(
                "data", videoSummaryDtos,
                "message", videoSummaryDtos.isEmpty() ? "No videos found with title '" + videoTitle + "'" : "Videos retrieved successfully"
        ));
    }



    // Helpers
    private Long getVideoDurationInSeconds(File videoFile) throws Exception {
        MultimediaObject multimediaObject = new MultimediaObject(videoFile);
        MultimediaInfo info = multimediaObject.getInfo();
        return info.getDuration() / 1000L;
    }
}