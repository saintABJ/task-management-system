package com.example.tasks_mgt_sys.entities.org.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrgResponseDto {
    private String orgName;
    private LocalDateTime createdDate;
}
