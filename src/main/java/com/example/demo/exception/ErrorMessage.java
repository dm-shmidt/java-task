package com.example.demo.exception;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ErrorMessage {
    public ErrorMessage(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    private String message;

    private LocalDateTime timestamp;
}
