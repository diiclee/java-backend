package com.example.java_backend.dto.request;

import com.example.java_backend.entity.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeTaskStatusRequest(
        @NotNull(message = "Status cannot be empty")
        TaskStatus status
) {}