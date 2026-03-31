package com.example.java_backend.util;

import com.example.java_backend.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public class RepositoryUtils {

    private RepositoryUtils() {}

    public static <T> T findByIdOrThrow(JpaRepository<T, Long> repository, Long id, String entityName) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(entityName + " with id " + id + " not found"));
    }
}