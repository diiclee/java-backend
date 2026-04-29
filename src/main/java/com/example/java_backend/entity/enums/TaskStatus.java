package com.example.java_backend.entity.enums;

public enum TaskStatus {
    OPEN,
    IN_PROGRESS,
    COMPLETED;

    public boolean canTransitionTo(TaskStatus next) {
        return switch (this) {
            case OPEN -> next == IN_PROGRESS;
            case IN_PROGRESS -> next == COMPLETED;
            case COMPLETED -> false;
        };
    }
}