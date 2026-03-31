package com.example.java_backend.dto.response;

public record ProjectResponse(
        Long id,
        String title,
        String description,
        Long ownerId,
        String ownerName
) {}