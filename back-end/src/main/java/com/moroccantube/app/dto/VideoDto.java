package com.moroccantube.app.dto;

import com.moroccantube.app.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoDto {
    private UUID id;
    private String title;
    private String description;
    private String filename;
    private String contentType;
    private Long size;
    private LocalDateTime uploadDate;
    private Long duration;
    private String uploaderUsername;

    private List<Comment> comments;
    private int likesCount;
    private int commentsCount;
}