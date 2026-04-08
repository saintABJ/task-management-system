package com.example.tasks_mgt_sys.entities.task.service;


import com.example.tasks_mgt_sys.entities.task.dto.TaskRequestDto;
import com.example.tasks_mgt_sys.entities.task.dto.TaskResponseDto;
import com.example.tasks_mgt_sys.entities.task.TaskStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TaskService {

    void createTask(Long projectId, TaskRequestDto request);

    List<TaskResponseDto> getUserTasks(String email);

    void assignTask(Long taskId, String email);

    void updateStatus(Long id, TaskStatus status, String userEmail);

    List<TaskResponseDto> getOverdueTask(String email);

    //Page<TaskResponseDto> getTasksByStatus(String status, int page, int size);

    void deleteTask(Long taskId);
}
