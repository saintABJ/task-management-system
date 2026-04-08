package com.example.tasks_mgt_sys.cli;

import com.example.tasks_mgt_sys.entities.org.dto.OrgRequestDto;
import com.example.tasks_mgt_sys.entities.org.service.OrgService;
import com.example.tasks_mgt_sys.entities.project.dto.ProjectRequestDto;
import com.example.tasks_mgt_sys.entities.project.dto.ProjectResponseDto;
import com.example.tasks_mgt_sys.entities.project.service.ProjectService;
import com.example.tasks_mgt_sys.entities.task.dto.TaskRequestDto;
import com.example.tasks_mgt_sys.entities.task.dto.TaskResponseDto;
import com.example.tasks_mgt_sys.entities.task.service.TaskService;
import com.example.tasks_mgt_sys.entities.user.dto.UserRequestDto;
import com.example.tasks_mgt_sys.entities.user.dto.UserResponseDto;
import com.example.tasks_mgt_sys.entities.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.time.LocalDateTime;
import java.util.List;


@ShellComponent
@RequiredArgsConstructor
public class CliCommands {

    private final TaskService taskService;
    private final UserService userService;
    private final CliSession cliSession;
    private final OrgService orgService;
    private final CliSecurityUtil cliSecurityUtil;
    private final ProjectService projectService;


  @ShellMethod(value = "Create a new admin", key = "admin-create")
    public String createAdmin(String name, String email, String password ) {
        userService.createUser(null, new UserRequestDto(name, email, password));
        return  "Admin Created";
    }

    @ShellMethod(value = "Create a new user", key = "user-create")
    public String createUser(String name, String email, String password, Long orgId) {
        userService.createUser(orgId, new UserRequestDto(name, email, password));
        return "User Created";
    }

    @ShellMethod(value = "login user", key = "login")
    public String login(String email, String password) {
      String token = userService.loginUser(UserRequestDto.builder()
              .email(email)
              .password(password)
              .build())
              .getTokenPair()
              .accessToken();

      cliSession.setToken(token);
      return "Logged in with Token: " + token;
    }

    @ShellMethod(value = "Create organization", key = "org-create")
    public String createOrg(String name, String createdAt) {
        if (!cliSession.isLoggedIn()) {
            return "Please login first!";
        }
        cliSecurityUtil.authenticate(cliSession.getToken());
        LocalDateTime parsedDate = LocalDateTime.parse(createdAt);
        orgService.createOrg(new OrgRequestDto(name, parsedDate));
        return "Organization created!";
    }

    @ShellMethod(value = "Create project", key = "project-create")
    public String createProject(String title, String description) {

        if (!cliSession.isLoggedIn()) {
            return "Please login first!";
        }

        cliSecurityUtil.authenticate(cliSession.getToken());
        ProjectRequestDto dto = new ProjectRequestDto(title, description);
        projectService.createProject(dto);
        return "Project created Successfully! ";
    }

    @ShellMethod(value = "List projects", key = "project-list")
    public String listProjects(int page, int size) {

        if (!cliSession.isLoggedIn()) {
            return "Please login first!";
        }

        cliSecurityUtil.authenticate(cliSession.getToken());

        Page<ProjectResponseDto> projects = projectService.getProjects(page, size);

        return projects.getContent().toString();
    }

    @ShellMethod(value = "Get project by ID", key = "project-get")
    public String getProject(Long projectId) {

      // You don't need neither authentication nor authorization to use this endpoint
       /* if (!cliSession.isLoggedIn()) {
            return "Please login first!";
        }

        cliSecurityUtil.authenticate(cliSession.getToken());*/

        ProjectResponseDto project = projectService.getProject(projectId);

        return project.toString();
    }


    @ShellMethod(value = "Create task", key = "task-create")
    public String createTask(String title, String description, Long projectId, LocalDateTime dueDate) {

        if (!cliSession.isLoggedIn()) {
            return "Please login first!";
        }

        cliSecurityUtil.authenticate(cliSession.getToken());

        TaskRequestDto dto = new TaskRequestDto(title, description, dueDate);

        /*TaskResponseDto response = */taskService.createTask(projectId, dto);

        return "Task created: " /*+ response.getTitle()*/;
    }


    @ShellMethod("Assign task")
    public String assignTask(Long taskId, String email) {
        cliSecurityUtil.authenticate(cliSession.getToken());
        taskService.assignTask(taskId, email);
        return "Task assigned!";
    }

    @ShellMethod(value = "List tasks", key = "task-list")
    public String listTasks(String status, int page, int size) {

        if (!cliSession.isLoggedIn()) {
            return "Please login first!";
        }

        cliSecurityUtil.authenticate(cliSession.getToken());

        List<TaskResponseDto> tasks = taskService.getUserTasks(status/*, page, size*/);

        return tasks/*.getContent()*/.toString();
    }

    @ShellMethod(value = "List users in my organization", key = "org-users")
    public String getOrgUsers() {

        if (!cliSession.isLoggedIn()) {
            return "Please login first!";
        }

        cliSecurityUtil.authenticate(cliSession.getToken());

        List<UserResponseDto> users = orgService.viewOrgUsers();

        return users.toString();
    }
}
