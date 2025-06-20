package com.moroccantube.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoSummaryDto {
    private UUID id;
    private String title;
    private LocalDateTime uploadDate;
    private String username;
    private int commentCount;
    private int likeCount;
    private boolean likedByCurrentUser;
    private Long duration;
}
