package nl.novi.event_management_system.dtos.ticketDtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import nl.novi.event_management_system.enums.TicketType;
import nl.novi.event_management_system.validators.ticketType.ValidTicketType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class TicketCreateDTO {
    @NotNull(message = "Price cannot be empty.")
    private BigDecimal price;
    @NotNull(message = "purchaseDate cannot be empty.")
    @PastOrPresent(message = "Purchase date cannot be in the future")
    private LocalDate purchaseDate = LocalDate.now();
    @Enumerated(EnumType.STRING)
    @ValidTicketType
    private TicketType ticketType;

    @Schema(description = "The ID of the event this ticket is for.", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "eventId cannot be empty.")
    private UUID eventId;
    @Schema(description = "The username of the user who purchased this ticket. This can be empty. Since, we have to create ticket so, the user can buy it", example = "jack")
    private String username;
}