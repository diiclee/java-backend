package com.example.java_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddCommentRequest(

        @NotBlank(message = "Content cannot be empty")
        String content,

        @NotNull(message = "Author id cannot be empty")
        Long authorId
) {}