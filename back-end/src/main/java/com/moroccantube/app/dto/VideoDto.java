package com.moroccantube.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDto {

    private Long id;
    private String title;
    private String description;
    private String filename;
    private String contentType;
    private Long size;
    private LocalDateTime uploadDate;
    private Long userId;
    private String username;
    private int commentCount;
    private int likeCount;
    private boolean likedByCurrentUser;

}
