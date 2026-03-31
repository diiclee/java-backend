package com.example.java_backend.dto.request;

import com.example.java_backend.entity.enums.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateTaskRequest(

        @NotBlank(message = "There must be a table")
        String title,

        String description,

        @NotNull(message = "Priority cannot be empty")
        TaskPriority priority,

        LocalDate dueDate,

        @NotNull(message = "Project id cannot be empty")
        Long projectId
) {}