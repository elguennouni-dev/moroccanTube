package com.moroccantube.app.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private String username;
}
