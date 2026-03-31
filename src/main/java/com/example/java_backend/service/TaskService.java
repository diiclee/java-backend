package com.example.java_backend.service;

import com.example.java_backend.dto.request.CreateTaskRequest;
import com.example.java_backend.dto.request.UpdateTaskRequest;
import com.example.java_backend.dto.response.TaskResponse;
import com.example.java_backend.exception.ResourceNotFoundException;
import com.example.java_backend.mapper.TaskMapper;
import com.example.java_backend.repository.ProjectRepository;
import com.example.java_backend.repository.TaskRepository;
import com.example.java_backend.util.RepositoryUtils;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository,
                       ProjectRepository projectRepository,
                       TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.taskMapper = taskMapper;
    }

    public TaskResponse createTask(CreateTaskRequest request) {
        var project = RepositoryUtils.findByIdOrThrow(projectRepository, request.projectId(), "Project");
        var task = taskMapper.toEntity(request, project);
        var savedTask = taskRepository.save(task);
        return taskMapper.toResponse(savedTask);
    }

    public TaskResponse getTask(Long id) {
        var task = RepositoryUtils.findByIdOrThrow(taskRepository, id, "Task");
        return taskMapper.toResponse(task);
    }

    public TaskResponse updateTask(Long id, UpdateTaskRequest request) {
        var task = RepositoryUtils.findByIdOrThrow(taskRepository, id, "Task");
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setPriority(request.priority());
        task.setDueDate(request.dueDate());
        var savedTask = taskRepository.save(task);
        return taskMapper.toResponse(savedTask);
    }

    public void deleteTask(Long id) {
        var task = RepositoryUtils.findByIdOrThrow(taskRepository, id, "Task");
        taskRepository.delete(task);
    }
}