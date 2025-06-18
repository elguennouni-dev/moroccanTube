package com.moroccantube.app.controller;

import com.moroccantube.app.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

@RestController
@RequestMapping("/api/video")
public class VideoController {

    @Autowired
    private StorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(@RequestParam("file")MultipartFile file) {
        String filename = storageService.store(file);
        return ResponseEntity.ok("Upload: " + filename);
    }

    @GetMapping("/watch/{filename}")
    public ResponseEntity<Resource> watchVideo(@PathVariable String filename) {
        Resource resource = storageService.loadAsResource(filename);

        return ResponseEntity.ok()
                .contentType(MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(resource);
    }

}
