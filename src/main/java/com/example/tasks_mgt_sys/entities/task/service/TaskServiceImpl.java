package com.example.tasks_mgt_sys.entities.task.service;

import com.example.tasks_mgt_sys.entities.Audit.Audit;
import com.example.tasks_mgt_sys.entities.Audit.AuditAction;
import com.example.tasks_mgt_sys.entities.Audit.AuditLog;
import com.example.tasks_mgt_sys.entities.Audit.AuditLogRepository;
import com.example.tasks_mgt_sys.entities.Audit.service.AuditLogService;
import com.example.tasks_mgt_sys.entities.task.dto.TaskRequestDto;
import com.example.tasks_mgt_sys.entities.task.dto.TaskResponseDto;
import com.example.tasks_mgt_sys.entities.task.Task;
import com.example.tasks_mgt_sys.entities.task.TaskRepository;
import com.example.tasks_mgt_sys.entities.task.TaskStatus;
import com.example.tasks_mgt_sys.exceptions.ConflictException;
import com.example.tasks_mgt_sys.exceptions.CustomBadRequestException;
import com.example.tasks_mgt_sys.exceptions.EntityNotFoundException;
import com.example.tasks_mgt_sys.entities.project.Project;
import com.example.tasks_mgt_sys.entities.project.ProjectRepository;
import com.example.tasks_mgt_sys.entities.role.RoleRepository;
import com.example.tasks_mgt_sys.entities.user.UserRepository;
import com.example.tasks_mgt_sys.entities.role.Role;
import com.example.tasks_mgt_sys.entities.role.RoleName;
import com.example.tasks_mgt_sys.entities.user.CurrentUserUtil;
import com.example.tasks_mgt_sys.entities.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CurrentUserUtil currentUserUtil;
    private final AuditLogService auditLogService;

    @Override
    public void createTask(Long projectId, TaskRequestDto request) {

        Project project = projectRepository.getProjectById(projectId).orElseThrow(()
                -> new EntityNotFoundException("Project not found"));

        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .status(TaskStatus.TODO)
                .project(project)
                .dueDate(request.dueDate())
                .build();

        taskRepository.save(task);

    }

    @Override
    public List<TaskResponseDto> getUserTasks(String email) {

        User user = getUser(email);

        return taskRepository.getTasksByUser(user).stream().map(
                task -> TaskResponseDto.builder()
                        .id(task.getId())
                        .title(task.getTitle())
                        .description(task.getDescription())
                        .status(task.getStatus())
                        .dueDate(task.getDueDate())
                        .build()).toList();
    }


    @Transactional
    @Override
    public void assignTask(Long taskId, String email) {

        User user = userRepository.findByEmailIgnoreCase(email).orElseThrow(() ->
                new EntityNotFoundException("User not found"));

        Task task = taskRepository.getTaskById(taskId).orElseThrow(() -> new EntityNotFoundException("Task not found"));

        if (user.getTasks().contains(task)) throw new ConflictException("Task already assigned to user");

        List<Task> tasks = user.getTasks();
        tasks.add(task);
        task.setUser(user);
    }

    @Transactional
    @Override
    public void updateStatus(Long id, TaskStatus status, String userEmail) {

        User user = getUser(userEmail);

        Task task = taskRepository.findTaskByIdAndUser(id, user).orElseThrow(()
                -> new EntityNotFoundException("Task not found"));

        if (task.getStatus() == TaskStatus.TODO && status == TaskStatus.DONE)
            throw new CustomBadRequestException("Only task marked as progress can be done");

        if (task.getStatus() == TaskStatus.DONE)
            throw new CustomBadRequestException("Task is already completed");

        if (task.getStatus() == status)
            throw new CustomBadRequestException("Task status not changed");

        task.setStatus(status);
    }


    @Override
    public List<TaskResponseDto> getOverdueTask(String email) {
        User user = getUser(email);

        List<Task> tasks = taskRepository.findByUserAndDueDateBeforeAndStatusNot(user, LocalDateTime.now(), TaskStatus.DONE);
        return tasks.stream().map(task -> TaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .dueDate(task.getDueDate())
                .build()).toList();
    }

    /*@Override
    public void deleteTask(Long taskId) {
        Task task = taskRepository.getTaskById(taskId).orElseThrow(() -> new EntityNotFoundException("Task not found"));
        task.setDeletedAt(LocalDateTime.now());
        taskRepository.save(task);

        auditLogRepository.save(
                AuditLog.builder()
                        .action("DELETE")
                        .entity("TASK")
                        .entityId(task.getId())
                        .performedBy(currentUserUtil.getLoggedInUsername())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }*/

    @Override
//    @Audit(action = "DELETE", entity = "TASK")
    public void deleteTask(Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        if (task.getDeletedAt() != null) {
            throw new RuntimeException("Task already deleted");
        }

        task.setDeletedAt(LocalDateTime.now());
        taskRepository.save(task);

        auditLogService.logAction(AuditAction.DELETE, "TASK", task.getId());
    }

   /* @Override
    public Page<TaskResponseDto> getTasksByStatus(String status, int page, int size) {

        User user = currentUserUtil.getLoggedInUser();

        Pageable pageable = PageRequest.of(page, size);

        Page<Task> tasks;

        if (status != null && !status.isBlank()) {

            TaskStatus taskStatus;

            try {
                taskStatus = TaskStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new CustomBadRequestException("Invalid task status: " + status);
            }

            tasks = taskRepository.findByOrganizationAndStatus(
                    user.getOrganization(),
                    taskStatus,
                    pageable
            );

        } else {

            tasks = taskRepository.findByOrganization(
                    user.getOrganization(),
                    pageable
            );
        }

        return tasks.map(task -> TaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .build());
    }*/

    private @NonNull User getUser(String email) {
        User user;
        User loggedInUser = currentUserUtil.getLoggedInUser();

        Role admin = roleRepository.findRoleByRole(RoleName.ADMIN).orElseThrow(() -> new CustomBadRequestException("Role mismatch"));
        Role manager = roleRepository.findRoleByRole(RoleName.MANAGER).orElseThrow(() -> new CustomBadRequestException("Role mismatch"));

        boolean hasPermission = loggedInUser.getRole().stream().anyMatch(role -> role == admin || role == manager);
        if (hasPermission) {
            user = userRepository.findByEmailIgnoreCase(email).orElseThrow(() ->
                    new EntityNotFoundException("User not found"));
        } else {
            user = loggedInUser;
        }
        return user;
    }

}
