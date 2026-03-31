package com.example.java_backend.service;

import com.example.java_backend.dto.request.CreateProjectRequest;
import com.example.java_backend.dto.response.ProjectResponse;
import com.example.java_backend.exception.ResourceNotFoundException;
import com.example.java_backend.mapper.ProjectMapper;
import com.example.java_backend.repository.ProjectRepository;
import com.example.java_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.example.java_backend.util.RepositoryUtils;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;

    public ProjectService(ProjectRepository projectRepository,
                          UserRepository userRepository,
                          ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectMapper = projectMapper;
    }

    public ProjectResponse createProject(CreateProjectRequest request) {
        var owner = userRepository.findById(request.ownerId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + request.ownerId() + " not found"));
        var project = projectMapper.toEntity(request, owner);
        var savedProject = projectRepository.save(project);
        return projectMapper.toResponse(savedProject);
    }

    public ProjectResponse getProject(Long id) {
        var project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project with id " + id + " not found"));
        return projectMapper.toResponse(project);
    }

    public List<ProjectResponse> listProjects() {
        return projectRepository.findAll()
                .stream()
                .map(projectMapper::toResponse)
                .toList();
    }

    public List<ProjectResponse> listProjectsByUser(Long ownerId) {
        RepositoryUtils.findByIdOrThrow(userRepository, ownerId, "User");
        return projectRepository.findByOwnerId(ownerId)
                .stream()
                .map(projectMapper::toResponse)
                .toList();
    }
}