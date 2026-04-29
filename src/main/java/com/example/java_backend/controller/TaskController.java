package com.example.java_backend.controller;

import com.example.java_backend.dto.request.AssignTaskRequest;
import com.example.java_backend.dto.request.ChangeTaskStatusRequest;
import com.example.java_backend.dto.request.CreateTaskRequest;
import com.example.java_backend.dto.request.UpdateTaskRequest;
import com.example.java_backend.dto.response.TaskResponse;
import com.example.java_backend.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(@Valid @RequestBody CreateTaskRequest request) {
        return taskService.createTask(request);
    }

    @GetMapping("/{id}")
    public TaskResponse getTask(@PathVariable Long id) {
        return taskService.getTask(id);
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequest request) {
        return taskService.updateTask(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @PatchMapping("/{id}/assign")
    public TaskResponse assignTask(
            @PathVariable Long id,
            @Valid @RequestBody AssignTaskRequest request) {
        return taskService.assignTask(id, request);
    }

    @PatchMapping("/{id}/unassign")
    public TaskResponse unassignTask(@PathVariable Long id){
        return taskService.unassignTask(id);
    }

    @PatchMapping("/{id}/status")
    public TaskResponse changeTaskStatus(
            @PathVariable Long id,
            @Valid @RequestBody ChangeTaskStatusRequest request) {
        return taskService.changeTaskStatus(id, request);
    }

    @GetMapping("/search")
    public List<TaskResponse> searchTasks(@RequestParam String keyword) {
        return taskService.searchTasks(keyword);
    }
}