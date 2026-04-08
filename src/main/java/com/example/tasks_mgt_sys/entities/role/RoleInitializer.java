package com.example.tasks_mgt_sys.entities.role;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer {

    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @PostConstruct
    public void initRoles() {
        for (var roleValue : RoleName.values()) {
            roleRepository.findRoleByRole(roleValue).orElseGet(() -> {
                Role role = Role.builder()
                        .role(roleValue)
                        .build();
                return roleRepository.save(role);
            });
        }
    }
}
