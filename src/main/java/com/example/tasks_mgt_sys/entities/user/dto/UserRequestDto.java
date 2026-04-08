package com.example.tasks_mgt_sys.entities.user.dto;

import com.example.tasks_mgt_sys.utils.view.UserView;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserRequestDto {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 20, message = "Name is too short")
    @JsonView(UserView.RegisterView.class)
    private String name;

    @Email(message = "Enter a valid email")
    @JsonView(UserView.LoginView.class)
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).*")
    @JsonView(UserView.LoginView.class)
    private String password;

//    @JsonView(UserView.RegisterView.class)
//    private Long organizationId;
}
