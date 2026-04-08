package com.example.tasks_mgt_sys.entities.project.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
@JsonPropertyOrder({"id", "title", "description"})
public class ProjectResponseDto {
    Long projectId;
    String title;
    String description;
//    List<Task> task;
}