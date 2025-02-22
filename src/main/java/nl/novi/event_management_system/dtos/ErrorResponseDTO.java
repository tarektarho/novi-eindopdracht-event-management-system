package nl.novi.event_management_system.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponseDTO {
    private String message;
    private int statusCode;
    private LocalDateTime timestamp;
}

