package com.example.tasks_mgt_sys.entities.task.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;


public record TaskRequestDto(@NotNull @NotBlank String title,
                             @NotNull @NotBlank String description,
                             @Future LocalDateTime dueDate){
}