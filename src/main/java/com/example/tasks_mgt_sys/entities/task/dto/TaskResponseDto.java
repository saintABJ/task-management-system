package com.example.tasks_mgt_sys.entities.task.dto;

import com.example.tasks_mgt_sys.entities.task.TaskStatus;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Builder
@Value
@JsonPropertyOrder({"id", "title", "description", "status", "dueDate"})
public class TaskResponseDto {
    Long id;
    String title;
    String description;
    TaskStatus status;
    LocalDateTime dueDate;
}