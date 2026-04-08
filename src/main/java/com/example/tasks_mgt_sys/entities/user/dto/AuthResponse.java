package com.example.tasks_mgt_sys.entities.user.dto;

import com.example.tasks_mgt_sys.utils.TokenPair;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private TokenPair tokenPair;
    private UserResponseDto user;
}
