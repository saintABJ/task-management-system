package com.example.tasks_mgt_sys.cli;

import org.springframework.stereotype.Component;

@Component
public class CliSession {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isLoggedIn() {
        return token != null;
    }
}
