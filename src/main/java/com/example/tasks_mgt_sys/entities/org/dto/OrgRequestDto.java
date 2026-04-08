package com.example.tasks_mgt_sys.entities.org.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrgRequestDto {
    @NotBlank(message = "Name is required")
    private String name;
    private LocalDateTime createdAt;
}
