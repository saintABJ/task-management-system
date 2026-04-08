package com.example.tasks_mgt_sys.entities.org.service;

import com.example.tasks_mgt_sys.entities.org.dto.OrgRequestDto;
import com.example.tasks_mgt_sys.entities.org.dto.OrgResponseDto;
import com.example.tasks_mgt_sys.entities.user.dto.UserResponseDto;

import java.util.List;


public interface OrgService {
    OrgResponseDto createOrg(OrgRequestDto orgRequestDto);
    List<UserResponseDto> viewOrgUsers();
}
