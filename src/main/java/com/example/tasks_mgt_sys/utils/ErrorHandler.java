package com.example.tasks_mgt_sys.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ErrorHandler {
    private String message;
    private int status;
    private long timestamp;


}
