package com.example.commentservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Long id;
    private Long taskId;
    private Long userId;
    private String text;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}