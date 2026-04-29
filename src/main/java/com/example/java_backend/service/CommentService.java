package com.example.java_backend.service;

import com.example.java_backend.dto.request.AddCommentRequest;
import com.example.java_backend.dto.response.CommentResponse;
import com.example.java_backend.mapper.CommentMapper;
import com.example.java_backend.repository.CommentRepository;
import com.example.java_backend.repository.TaskRepository;
import com.example.java_backend.repository.UserRepository;
import com.example.java_backend.util.RepositoryUtils;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    public CommentService(CommentRepository commentRepository,
                          TaskRepository taskRepository,
                          UserRepository userRepository,
                          CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
    }

    public CommentResponse addComment(Long taskId, AddCommentRequest request) {
        var task = RepositoryUtils.findByIdOrThrow(taskRepository, taskId, "Task");
        var author = RepositoryUtils.findByIdOrThrow(userRepository, request.authorId(), "User");
        var comment = commentMapper.toEntity(request, task, author);
        var savedComment = commentRepository.save(comment);
        return commentMapper.toResponse(savedComment);
    }

    public List<CommentResponse> getComments(Long taskId) {
        RepositoryUtils.findByIdOrThrow(taskRepository, taskId, "Task");
        return commentRepository.findByTaskId(taskId)
                .stream()
                .map(commentMapper::toResponse)
                .toList();
    }
}