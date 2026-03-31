package com.example.java_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateProjectRequest(

        @NotBlank(message = "There must be a title!")
        String title,
        String description,
        @NotNull(message = "There must be an owner. Owner ID should not be empty!")
        Long ownerId
) {}