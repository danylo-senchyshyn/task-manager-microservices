package com.example.taskservice.dto;

import com.example.taskservice.entity.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
}
