package com.example.java_backend.service;

import com.example.java_backend.dto.request.CreateUserRequest;
import com.example.java_backend.dto.response.UserResponse;
import com.example.java_backend.mapper.UserMapper;
import com.example.java_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.example.java_backend.exception.ResourceNotFoundException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponse createUser(CreateUserRequest request) {
        var user = userMapper.toEntity(request);
        var savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    public UserResponse getUser(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        return userMapper.toResponse(user);
    }
}