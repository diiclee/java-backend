package com.example.java_backend.controller;

import com.example.java_backend.dto.request.CreateProjectRequest;
import com.example.java_backend.dto.response.ProjectResponse;
import com.example.java_backend.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse createProject(@Valid @RequestBody CreateProjectRequest request) {
        return projectService.createProject(request);
    }

    @GetMapping("/{id}")
    public ProjectResponse getProject(@PathVariable Long id) {
        return projectService.getProject(id);
    }

    @GetMapping
    public List<ProjectResponse> listProjects() {
        return projectService.listProjects();
    }

    @GetMapping("/user/{ownerId}")
    public List<ProjectResponse> listProjectsByUser(@PathVariable Long ownerId) {
        return projectService.listProjectsByUser(ownerId);
    }
}