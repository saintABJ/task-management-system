package com.example.tasks_mgt_sys.entities.role.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpgradeRoleRequestDto(
        @NotNull @NotBlank String role
) {
}
