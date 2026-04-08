package com.example.tasks_mgt_sys.entities.task;

import com.example.tasks_mgt_sys.entities.task.dto.TaskRequestDto;
import com.example.tasks_mgt_sys.entities.task.dto.TaskResponseDto;
import com.example.tasks_mgt_sys.entities.task.service.TaskService;
import com.example.tasks_mgt_sys.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/task")
@RestController
public class TaskController {

    private final TaskService taskService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/{projectId}/create")
    public ResponseEntity<ApiResponse<String>> createTask(
            @PathVariable Long projectId, @RequestBody TaskRequestDto request) {

        taskService.createTask(projectId, request);
        return ResponseEntity.ok(ApiResponse.success("Task added successfully", null));
    }

    @GetMapping("/all-tasks")
    public ResponseEntity<ApiResponse<List<TaskResponseDto>>> getTasks(@RequestParam(required = false) String email) {

        List<TaskResponseDto> userTasks = taskService.getUserTasks(email);
        return ResponseEntity.ok(ApiResponse.success(userTasks));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PatchMapping("/{taskId}/assign/{email}")
    public ResponseEntity<ApiResponse<String>> assignTask(@PathVariable Long taskId, @PathVariable String email) {

        taskService.assignTask(taskId, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Task Successfully assigned", null));
    }

    @PatchMapping("/update-status")
    public ResponseEntity<ApiResponse<String>> updateStatus(
            @RequestParam Long id,
            @RequestParam String taskStatus,
            @RequestParam(required = false) String userEmail
    ) {

        taskService.updateStatus(id, TaskStatus.valueOf(taskStatus), userEmail);
        return ResponseEntity.ok(ApiResponse.success("Task status updated successfully", null));

    }

    @GetMapping("/overdue")
    public ResponseEntity<ApiResponse<List<TaskResponseDto>>> getOverdueTask(@RequestParam(required = false) String email) {

        List<TaskResponseDto> task = taskService.getOverdueTask(email);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(task));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTask(@PathVariable Long id) {

        taskService.deleteTask(id);

        return ResponseEntity.ok(ApiResponse.success("Task deleted successfully"));
    }

   /* @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/tasks-status")
    public ResponseEntity<ApiResponse<Page<TaskResponseDto>>> getTasksByStatus(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Page<TaskResponseDto> tasks =
                taskService.getTasksByStatus(status, page, size);

        return ResponseEntity.ok(ApiResponse.success(tasks));
    }*/

}
