package com.example.tasks_mgt_sys.entities.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserResponseDto {
    private UUID userId;
    private String name;
    private String email;
    private Long organizationId;
    private String organizationName;
}
