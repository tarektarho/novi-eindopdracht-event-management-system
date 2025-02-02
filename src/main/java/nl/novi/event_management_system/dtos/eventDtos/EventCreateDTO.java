package nl.novi.event_management_system.dtos.eventDtos;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class EventCreateDTO {

    @NotBlank(message = "Event name cannot be empty")
    @Size(min = 3, max = 100, message = "Event name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "organizer username cannot be empty.")
    private String organizerUsername;

    @NotBlank(message = "Location cannot be empty")
    @Size(min = 3, max = 200, message = "Location must be between 3 and 200 characters")
    private String location;

    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;

    @Min(value = 1, message = "Capacity must be at least 1")
    private int capacity;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private double price;
    private UUID ticketId;
    private UUID feedbackId;

}
