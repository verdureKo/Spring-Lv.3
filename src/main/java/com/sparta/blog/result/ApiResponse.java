package com.sparta.blog.result;

import lombok.Data;

@Data
public class ApiResponse {
    private int statusCode;
    private String message;

    public ApiResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}