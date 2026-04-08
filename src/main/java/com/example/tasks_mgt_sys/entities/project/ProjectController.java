package com.example.tasks_mgt_sys.entities.project;

import com.example.tasks_mgt_sys.entities.project.dto.ProjectRequestDto;
import com.example.tasks_mgt_sys.entities.project.dto.ProjectResponseDto;
import com.example.tasks_mgt_sys.entities.project.service.ProjectService;
import com.example.tasks_mgt_sys.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("api/v1")
@RestController
public class ProjectController {

    private final ProjectService projectService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create/project")
    public ResponseEntity<ApiResponse<String>> createProject(@RequestBody ProjectRequestDto request) {

        projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Project created successfully", null));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/projects")
    public ResponseEntity<ApiResponse<Page<ProjectResponseDto>>> getProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Page<ProjectResponseDto> projects = projectService.getProjects(page, size);

        return ResponseEntity.ok(ApiResponse.success(projects));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/project/{id}")
    public ResponseEntity<ApiResponse<ProjectResponseDto>> getProject(@PathVariable Long id) {

        ProjectResponseDto project = projectService.getProject(id);
        return ResponseEntity.ok(ApiResponse.success(project));
    }
}
