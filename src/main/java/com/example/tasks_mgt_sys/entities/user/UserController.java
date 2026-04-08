package com.example.tasks_mgt_sys.entities.user;

import com.example.tasks_mgt_sys.entities.user.dto.AuthResponse;
import com.example.tasks_mgt_sys.entities.role.dto.UpgradeRoleRequestDto;
import com.example.tasks_mgt_sys.entities.user.dto.UserRequestDto;
import com.example.tasks_mgt_sys.entities.user.dto.UserResponseDto;
import com.example.tasks_mgt_sys.entities.role.RoleName;
import com.example.tasks_mgt_sys.entities.user.service.UserService;
import com.example.tasks_mgt_sys.utils.ApiResponse;
import com.example.tasks_mgt_sys.utils.ResponseHandler;
import com.example.tasks_mgt_sys.utils.view.UserView;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/auth")
public class UserController {

    private final UserService userService;
    private final ResponseHandler responseHandler;

    public UserController(UserService userService, ResponseHandler responseHandler) {
        this.userService = userService;
        this.responseHandler = responseHandler;
    }

    @PostMapping("/user/create")
    public ResponseEntity<?> createUser(@RequestParam(required = false) Long orgId, @JsonView(UserView.RegisterView.class) @Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto response = userService.createUser(orgId, userRequestDto);
        //return responseHandler.responseBuilder("User Created Successfully", HttpStatus.CREATED, response);
        return new ResponseEntity<>(ApiResponse.success("User Created Successfully. Check your email for otp", response), HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/assign-role/{userId}")
    public ResponseEntity<ApiResponse<String>> addRole(@PathVariable String userId, @Valid @RequestBody UpgradeRoleRequestDto request) {
        userService.addRole(UUID.fromString(userId), RoleName.valueOf(request.role()));
        return ResponseEntity.ok(ApiResponse.success("Role added successfully", null));
    }

    @PostMapping("/user/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@JsonView(UserView.LoginView.class) @RequestBody UserRequestDto userRequestDto) {
        AuthResponse response = userService.loginUser(userRequestDto);
        return new ResponseEntity<>(ApiResponse.success("Login Successfully", response), HttpStatus.OK);

    }
}