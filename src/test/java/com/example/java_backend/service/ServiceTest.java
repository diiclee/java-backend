package com.example.java_backend.service;

import com.example.java_backend.dto.request.*;
import com.example.java_backend.entity.Comment;
import com.example.java_backend.entity.Project;
import com.example.java_backend.entity.Task;
import com.example.java_backend.entity.User;
import com.example.java_backend.entity.enums.TaskPriority;
import com.example.java_backend.entity.enums.TaskStatus;
import com.example.java_backend.exception.BadRequestException;
import com.example.java_backend.exception.ResourceNotFoundException;
import com.example.java_backend.mapper.CommentMapper;
import com.example.java_backend.mapper.ProjectMapper;
import com.example.java_backend.mapper.TaskMapper;
import com.example.java_backend.mapper.UserMapper;
import com.example.java_backend.repository.CommentRepository;
import com.example.java_backend.repository.ProjectRepository;
import com.example.java_backend.repository.TaskRepository;
import com.example.java_backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceTest {

    // Mocks
    @Mock private UserRepository userRepository;
    @Mock private UserMapper userMapper;
    @InjectMocks private UserService userService;

    @Mock private ProjectRepository projectRepository;
    @Mock private ProjectMapper projectMapper;
    @InjectMocks private ProjectService projectService;

    @Mock private TaskRepository taskRepository;
    @Mock private TaskMapper taskMapper;
    @InjectMocks private TaskService taskService;

    @Mock private CommentRepository commentRepository;
    @Mock private CommentMapper commentMapper;
    @InjectMocks private CommentService commentService;

    //Helpers
    private User dummyUser() { return new User(1L, "Alice", "alice@example.com"); }
    private Project dummyProject() { return new Project("Project", null, dummyUser()); }
    private Task dummyTask() { return new Task("Task 1", null, TaskPriority.HIGH, null, dummyProject()); }
    private Comment dummyComment() {return new Comment("Nice task", dummyTask(), dummyUser());}

    //UC1: Create User
    @Test
    void createUser_shouldReturnUserResponse() {
        var request = new CreateUserRequest("Alice", "alice@example.com");
        var user = dummyUser();
        when(userMapper.toEntity(request)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenCallRealMethod();

        assertThat(userService.createUser(request).name()).isEqualTo("Alice");
    }

    // UC2: Get User
    @Test
    void getUser_shouldReturnUserResponse() {
        var user = dummyUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenCallRealMethod();

        assertThat(userService.getUser(1L).name()).isEqualTo("Alice");
    }

    @Test
    void getUser_shouldThrowWhenNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUser(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    //UC3: Create Project
    @Test
    void createProject_shouldReturnProjectResponse() {
        var request = new CreateProjectRequest("Project", null, 1L);
        var owner = dummyUser();
        var project = dummyProject();
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(projectMapper.toEntity(request, owner)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.toResponse(project)).thenCallRealMethod();

        assertThat(projectService.createProject(request).title()).isEqualTo("Project");
    }

    @Test
    void createProject_shouldThrowWhenOwnerNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectService.createProject(new CreateProjectRequest("Project", null, 99L)))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // UC4: Get Project
    @Test
    void getProject_shouldThrowWhenNotFound() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectService.getProject(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    //UC5: List Projects
    @Test
    void listProjects_shouldReturnAllProjects() {
        var project = dummyProject();
        when(projectRepository.findAll()).thenReturn(List.of(project));
        when(projectMapper.toResponse(project)).thenCallRealMethod();

        assertThat(projectService.listProjects()).hasSize(1);
    }

    @Test
    void listProjectsByUser_shouldThrowWhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectService.listProjectsByUser(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // UC6: Create Task
    @Test
    void createTask_shouldReturnTaskResponse() {
        var request = new CreateTaskRequest("Task 1", null, TaskPriority.HIGH, null, 1L);
        var project = dummyProject();
        var task = dummyTask();
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskMapper.toEntity(request, project)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponse(task)).thenCallRealMethod();

        assertThat(taskService.createTask(request).title()).isEqualTo("Task 1");
    }

    @Test
    void createTask_shouldThrowWhenProjectNotFound() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.createTask(new CreateTaskRequest("Task 1", null, TaskPriority.HIGH, null, 99L)))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // UC7: Get Task
    @Test
    void getTask_shouldThrowWhenNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTask(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // UC8: Update Task
    @Test
    void updateTask_shouldReturnUpdatedTaskResponse() {
        var task = dummyTask();
        var request = new UpdateTaskRequest("New Title", null, TaskPriority.LOW, null);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponse(task)).thenCallRealMethod();

        assertThat(taskService.updateTask(1L, request).title()).isEqualTo("New Title");
    }

    @Test
    void updateTask_shouldThrowWhenNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.updateTask(99L, new UpdateTaskRequest("New Title", null, TaskPriority.LOW, null)))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    //  UC9: Delete Task
    @Test
    void deleteTask_shouldDeleteSuccessfully() {
        var task = dummyTask();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThatCode(() -> taskService.deleteTask(1L)).doesNotThrowAnyException();
        verify(taskRepository).delete(task);
    }

    @Test
    void deleteTask_shouldThrowWhenNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.deleteTask(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    //UC10: Assign Task
    @Test
    void assignTask_shouldReturnTaskResponse() {
        var task = dummyTask();
        var user = dummyUser();
        var request = new AssignTaskRequest(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponse(task)).thenCallRealMethod();

        assertThat(taskService.assignTask(1L, request).title()).isEqualTo("Task 1");
    }

    @Test
    void assignTask_shouldThrowWhenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.assignTask(99L, new AssignTaskRequest(1L)))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // UC11: Unassign Task
    @Test
    void unassignTask_shouldReturnTaskResponse() {
        var task = dummyTask();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponse(task)).thenCallRealMethod();

        assertThatCode(() -> taskService.unassignTask(1L)).doesNotThrowAnyException();
    }

    @Test
    void unassignTask_shouldThrowWhenNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.unassignTask(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // UC12: Change Task Status
    @Test
    void changeTaskStatus_shouldReturnUpdatedStatus() {
        var task = dummyTask();
        var request = new ChangeTaskStatusRequest(TaskStatus.IN_PROGRESS);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponse(task)).thenCallRealMethod();

        assertThatCode(() -> taskService.changeTaskStatus(1L, request)).doesNotThrowAnyException();
    }

    @Test
    void changeTaskStatus_shouldThrowWhenTransitionNotAllowed() {
        var task = dummyTask(); // status is OPEN by default
        var request = new ChangeTaskStatusRequest(TaskStatus.COMPLETED);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThatThrownBy(() -> taskService.changeTaskStatus(1L, request))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void changeTaskStatus_shouldThrowWhenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.changeTaskStatus(99L, new ChangeTaskStatusRequest(TaskStatus.IN_PROGRESS)))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // UC13: Add Comment
    @Test
    void addComment_shouldReturnCommentResponse() {
        var request = new AddCommentRequest("Nice task", 1L);
        var task = dummyTask();
        var author = dummyUser();
        var comment = dummyComment();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(1L)).thenReturn(Optional.of(author));
        when(commentMapper.toEntity(request, task, author)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.toResponse(comment)).thenCallRealMethod();

        assertThatCode(() -> commentService.addComment(1L, request)).doesNotThrowAnyException();
    }

    @Test
    void addComment_shouldThrowWhenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.addComment(99L, new AddCommentRequest("Nice task", 1L)))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void addComment_shouldThrowWhenAuthorNotFound() {
        var task = dummyTask();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.addComment(1L, new AddCommentRequest("Nice task", 99L)))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    //UC14: View Comments
    @Test
    void getComments_shouldReturnComments() {
        var task = dummyTask();
        var comment = dummyComment();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(commentRepository.findByTaskId(1L)).thenReturn(List.of(comment));
        when(commentMapper.toResponse(comment)).thenCallRealMethod();

        assertThat(commentService.getComments(1L)).hasSize(1);
    }

    @Test
    void getComments_shouldThrowWhenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.getComments(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // UC15: Search Tasks
    @Test
    void searchTasks_shouldReturnMatchingTasks() {
        var task = dummyTask();
        when(taskRepository.findByTitleContainingIgnoreCase("Task")).thenReturn(List.of(task));
        when(taskMapper.toResponse(task)).thenCallRealMethod();

        assertThat(taskService.searchTasks("Task")).hasSize(1);
    }

    @Test
    void searchTasks_shouldReturnEmptyWhenNoMatch() {
        when(taskRepository.findByTitleContainingIgnoreCase("xyz")).thenReturn(List.of());

        assertThat(taskService.searchTasks("xyz")).isEmpty();
    }
}