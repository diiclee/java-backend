package com.example.java_backend.mapper;

import com.example.java_backend.dto.request.AddCommentRequest;
import com.example.java_backend.dto.response.CommentResponse;
import com.example.java_backend.entity.Comment;
import com.example.java_backend.entity.Task;
import com.example.java_backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public Comment toEntity(AddCommentRequest request, Task task, User author) {
        return new Comment(request.content(), task, author);
    }

    public CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getTask().getId(),
                comment.getAuthor().getId(),
                comment.getAuthor().getName()
        );
    }
}