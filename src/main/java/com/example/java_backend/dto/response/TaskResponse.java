package com.example.java_backend.dto.response;

import com.example.java_backend.entity.enums.TaskPriority;
import com.example.java_backend.entity.enums.TaskStatus;
import java.time.LocalDate;

public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        LocalDate dueDate,
        Long projectId,
        Long assignedUserId,
        String assignedUserName
) {}