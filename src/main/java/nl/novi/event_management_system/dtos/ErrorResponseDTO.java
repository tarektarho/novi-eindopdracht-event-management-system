package nl.novi.event_management_system.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponseDTO {
    private String message;
    private int statusCode;
    private LocalDateTime timestamp;

    public ErrorResponseDTO(String message, int statusCode, LocalDateTime timestamp) {
        this.message = message;
        this.statusCode = statusCode;
        this.timestamp = timestamp;
    }
}

