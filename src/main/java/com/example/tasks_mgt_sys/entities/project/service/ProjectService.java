package com.example.tasks_mgt_sys.entities.project.service;

import com.example.tasks_mgt_sys.entities.project.dto.ProjectRequestDto;
import com.example.tasks_mgt_sys.entities.project.dto.ProjectResponseDto;
import org.springframework.data.domain.Page;

public interface ProjectService {
    void createProject(ProjectRequestDto request);

    Page<ProjectResponseDto> getProjects(int page, int size);

    ProjectResponseDto getProject(Long id);

}
