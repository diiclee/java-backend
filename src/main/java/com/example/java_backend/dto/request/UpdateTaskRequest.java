package com.example.java_backend.dto.request;

import com.example.java_backend.entity.enums.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record UpdateTaskRequest(

        @NotBlank(message = "There must be a title")
        String title,

        String description,

        @NotNull(message = "Priority must be set")
        TaskPriority priority,

        LocalDate dueDate
) {}