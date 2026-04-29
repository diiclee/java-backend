package com.example.java_backend.dto.response;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        LocalDateTime createdAt,
        Long taskId,
        Long authorId,
        String authorName
) {}