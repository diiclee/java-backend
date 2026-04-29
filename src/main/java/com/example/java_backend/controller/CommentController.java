package com.example.java_backend.controller;

import com.example.java_backend.dto.request.AddCommentRequest;
import com.example.java_backend.dto.response.CommentResponse;
import com.example.java_backend.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tasks/{taskId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse addComment(
            @PathVariable Long taskId,
            @Valid @RequestBody AddCommentRequest request) {
        return commentService.addComment(taskId, request);
    }

    @GetMapping
    public List<CommentResponse> getComments(@PathVariable Long taskId) {
        return commentService.getComments(taskId);
    }
}