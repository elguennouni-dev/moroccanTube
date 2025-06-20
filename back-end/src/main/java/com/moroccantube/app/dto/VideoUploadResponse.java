package com.moroccantube.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoUploadResponse {
    private UUID videoId;
    private String title;
    private String message;
}
