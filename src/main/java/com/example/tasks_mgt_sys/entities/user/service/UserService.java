package com.example.tasks_mgt_sys.entities.user.service;

import com.example.tasks_mgt_sys.entities.user.dto.AuthResponse;
import com.example.tasks_mgt_sys.entities.user.dto.UserRequestDto;
import com.example.tasks_mgt_sys.entities.user.dto.UserResponseDto;
import com.example.tasks_mgt_sys.entities.role.RoleName;

import java.util.UUID;

public interface UserService {
    UserResponseDto createUser(Long orgId, UserRequestDto userRequestDto);
    /*List<UserResponseDto> getUsersByOrganization(Long organizationId);*/
    void addRole(UUID userID, RoleName role);
    AuthResponse loginUser(UserRequestDto userRequestDto);
}
