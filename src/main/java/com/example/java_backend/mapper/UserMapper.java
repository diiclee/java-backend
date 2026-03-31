package com.example.java_backend.mapper;

import com.example.java_backend.dto.request.CreateUserRequest;
import com.example.java_backend.dto.response.UserResponse;
import com.example.java_backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(CreateUserRequest request) {
        return new User(request.getName(), request.getEmail());
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}