package com.aevorix.user_service.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ApiErrorResponse {

    private int status;
    private String message;
    private Object response;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public ApiErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiErrorResponse(HttpStatus status, String message, Object response) {
        this();
        this.status = status.value();
        this.message = message;
        this.response = response;
    }

}
