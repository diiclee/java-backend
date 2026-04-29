package com.example.java_backend.service;

import com.example.java_backend.dto.request.AssignTaskRequest;
import com.example.java_backend.dto.request.ChangeTaskStatusRequest;
import com.example.java_backend.dto.request.CreateTaskRequest;
import com.example.java_backend.dto.request.UpdateTaskRequest;
import com.example.java_backend.dto.response.TaskResponse;
import com.example.java_backend.exception.BadRequestException;
import com.example.java_backend.exception.ResourceNotFoundException;
import com.example.java_backend.mapper.TaskMapper;
import com.example.java_backend.repository.ProjectRepository;
import com.example.java_backend.repository.TaskRepository;
import com.example.java_backend.repository.UserRepository;
import com.example.java_backend.util.RepositoryUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository,
                       ProjectRepository projectRepository,
                       UserRepository userRepository,
                       TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
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

    public TaskResponse assignTask(Long id, AssignTaskRequest request) {
        var task = RepositoryUtils.findByIdOrThrow(taskRepository, id, "Task");
        var user = RepositoryUtils.findByIdOrThrow(userRepository, request.userId(), "User");
        task.setAssignedUser(user);
        var savedTask = taskRepository.save(task);
        return taskMapper.toResponse(savedTask);
    }

    public TaskResponse unassignTask(Long id) {
        var task = RepositoryUtils.findByIdOrThrow(taskRepository, id, "Task");
        task.setAssignedUser(null);
        var savedTask = taskRepository.save(task);
        return taskMapper.toResponse(savedTask);
    }

    public TaskResponse changeTaskStatus(Long id, ChangeTaskStatusRequest request) {
        var task = RepositoryUtils.findByIdOrThrow(taskRepository, id, "Task");
        if (!task.getStatus().canTransitionTo(request.status())) {
            throw new BadRequestException("Status transition from " + task.getStatus() + " to " + request.status() + " is not allowed");
        }
        task.setStatus(request.status());
        var savedTask = taskRepository.save(task);
        return taskMapper.toResponse(savedTask);
    }

    public List<TaskResponse> searchTasks(String keyword) {
        return taskRepository.findByTitleContainingIgnoreCase(keyword)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }
}