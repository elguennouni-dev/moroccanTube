package com.moroccantube.app.controller;

import com.moroccantube.app.dao.VideoUploadRequest;
import com.moroccantube.app.dto.VideoUploadResponse;
import com.moroccantube.app.model.Video;
import com.moroccantube.app.service.StorageService;
import com.moroccantube.app.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/video")
public class VideoController {

    @Autowired
    private StorageService storageService;
    @Autowired
    private VideoService videoService;


    @GetMapping("/all")
    public ResponseEntity<?> getAllVideos() {
        return videoService.getAll();
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadVideo(
            @RequestPart("file") MultipartFile file,
            @RequestPart("videoInfo") VideoUploadRequest videoUploadRequest,
            Principal principal) {

        if (principal == null || principal.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "User not authenticated"));
        }

        VideoUploadResponse response = videoService.uploadVideo(file, videoUploadRequest, principal.getName());
        return ResponseEntity.ok(response);
    }



    @GetMapping("/watch/{videoId}")
    public ResponseEntity<?> watchVideo(@PathVariable UUID videoId) {
        return videoService.getVideoById(videoId);
    }


    @GetMapping("/search/{videoTitle}")
    public ResponseEntity<?> searchByTitle(@PathVariable String videoTitle) {
        return videoService.searchByTitle(videoTitle);
    }

}
