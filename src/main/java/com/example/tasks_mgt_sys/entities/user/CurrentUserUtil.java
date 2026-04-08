package com.example.tasks_mgt_sys.entities.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CurrentUserUtil {

    private final UserRepository userRepository;

    public CurrentUserUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getLoggedInUser() {

        String username = getLoggedInUsername();

        return userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new BadCredentialsException("Unauthorized"));
    }

    public String getLoggedInUsername() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();

    }

}
