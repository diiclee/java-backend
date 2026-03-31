package com.example.java_backend.mapper;

import com.example.java_backend.dto.request.CreateTaskRequest;
import com.example.java_backend.dto.response.TaskResponse;
import com.example.java_backend.entity.Project;
import com.example.java_backend.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toEntity(CreateTaskRequest request, Project project) {
        return new Task(
                request.title(),
                request.description(),
                request.priority(),
                request.dueDate(),
                project
        );
    }

    public TaskResponse toResponse(Task task) {
        var assignedUser = task.getAssignedUser();
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getProject().getId(),
                assignedUser != null ? assignedUser.getId() : null,
                assignedUser != null ? assignedUser.getName() : null
        );
    }
}