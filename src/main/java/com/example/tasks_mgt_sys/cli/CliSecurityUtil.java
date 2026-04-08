package com.example.tasks_mgt_sys.cli;

import com.example.tasks_mgt_sys.services.CustomUserDetailService;
import com.example.tasks_mgt_sys.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CliSecurityUtil {

    private final JwtService jwtService;
    private final CustomUserDetailService userDetailsService;

    public void authenticate(String token) {

        String email = jwtService.extractUsernameFromToken(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}

