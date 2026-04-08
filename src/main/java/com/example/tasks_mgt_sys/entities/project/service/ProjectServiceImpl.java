package com.example.tasks_mgt_sys.entities.project.service;

import com.example.tasks_mgt_sys.entities.project.dto.ProjectRequestDto;
import com.example.tasks_mgt_sys.entities.project.dto.ProjectResponseDto;
import com.example.tasks_mgt_sys.entities.project.Project;
import com.example.tasks_mgt_sys.entities.project.ProjectRepository;
import com.example.tasks_mgt_sys.exceptions.EntityNotFoundException;
import com.example.tasks_mgt_sys.entities.role.RoleRepository;
import com.example.tasks_mgt_sys.entities.role.Role;
import com.example.tasks_mgt_sys.entities.role.RoleName;
import com.example.tasks_mgt_sys.entities.user.CurrentUserUtil;
import com.example.tasks_mgt_sys.entities.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final RoleRepository roleRepository;
    private final CurrentUserUtil currentUserUtil;


    @Override
    public void createProject(ProjectRequestDto request) {

        User user = currentUserUtil.getLoggedInUser();

        Role adminRole = roleRepository.findRoleByRole(RoleName.ADMIN).orElseThrow(() -> new EntityNotFoundException("Role mismatch"));
        Role managerRole = roleRepository.findRoleByRole(RoleName.MANAGER).orElseThrow(() -> new EntityNotFoundException("Role mismatch"));


        if (!(user.getRole().contains(adminRole) || user.getRole().contains(managerRole)))
            throw new AccessDeniedException("You don't have permission for this operation");
        Project project = Project.builder()
                .title(request.title())
                .description(request.description())
                .user(user)
                .build();

        project.setOrganization(user.getOrganization());

        projectRepository.save(project);

    }

    @Override
    public Page<ProjectResponseDto> getProjects(int page, int size) {

        User user = currentUserUtil.getLoggedInUser();

        Pageable pageable = PageRequest.of(page, size);

        Page<Project> projects = projectRepository
                .findByOrganization(user.getOrganization(), pageable);

        return projects.map(project -> ProjectResponseDto.builder()
                .projectId(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .build());
    }

    @Override
    public ProjectResponseDto getProject(Long id) {
        Project project = projectRepository.getProjectById(id).orElseThrow(()
                -> new EntityNotFoundException("Project not found"));
        return ProjectResponseDto.builder()
                .projectId(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .build();
    }
}
