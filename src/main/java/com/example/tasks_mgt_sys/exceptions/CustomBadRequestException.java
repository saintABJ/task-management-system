package com.example.tasks_mgt_sys.exceptions;

public class CustomBadRequestException extends RuntimeException {

    public CustomBadRequestException(String message) {
        super(message);
    }
}
