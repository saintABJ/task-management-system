package com.example.tasks_mgt_sys.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;


@Component
public class ResponseHandler {

    public  ResponseEntity<Object> responseBuilder(String message, HttpStatus status, Object data) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", status.value());
        response.put("message", message);

        if (data != null) {
            response.put("data", data);
        }

        return new ResponseEntity<>(response, status);
    }
}

