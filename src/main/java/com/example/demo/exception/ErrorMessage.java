package com.example.demo.exception;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ErrorMessage {
    public ErrorMessage(String error) {
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }

    private String error;

    private LocalDateTime timestamp;
}
