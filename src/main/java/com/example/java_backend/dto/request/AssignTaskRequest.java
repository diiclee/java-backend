package com.example.java_backend.dto.request;

import jakarta.validation.constraints.NotNull;

public record AssignTaskRequest(
        @NotNull(message = "User id cannot be empty")
        Long userId
) {}