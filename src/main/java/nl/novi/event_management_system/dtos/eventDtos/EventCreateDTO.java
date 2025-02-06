package nl.novi.event_management_system.dtos.eventDtos;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class EventCreateDTO {
    private UUID id;
    @NotBlank(message = "Event name cannot be empty")
    @Size(min = 3, max = 100, message = "Event name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Location cannot be empty")
    @Size(min = 3, max = 200, message = "Location must be between 3 and 200 characters")
    private String location;

    @NotNull(message = "Start date is required")
    @Future(message = "Start date must be in the future")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    @Min(value = 1, message = "Capacity must be at least 1")
    private int capacity;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private double price;

    private String organizerUsername;
    private UUID ticketId;
    private UUID feedbackId;
}
