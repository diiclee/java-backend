package com.example.java_backend.mapper;

import com.example.java_backend.dto.request.CreateProjectRequest;
import com.example.java_backend.dto.response.ProjectResponse;
import com.example.java_backend.entity.Project;
import com.example.java_backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public Project toEntity(CreateProjectRequest request, User owner) {
        return new Project(request.title(), request.description(), owner);
    }

    public ProjectResponse toResponse(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getOwner().getId(),
                project.getOwner().getName()
        );
    }
}