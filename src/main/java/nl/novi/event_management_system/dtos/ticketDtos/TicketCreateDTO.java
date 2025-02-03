package nl.novi.event_management_system.dtos.ticketDtos;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import nl.novi.event_management_system.enums.TicketType;
import nl.novi.event_management_system.validators.ticketType.ValidTicketType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TicketCreateDTO {
    @NotNull(message = "Price cannot be empty.")
    private BigDecimal price;
    private String ticketCode = generateTicketCode();
    @NotNull(message = "purchaseDate cannot be empty.")
    private LocalDateTime purchaseDate;
    @Enumerated(EnumType.STRING)
    @ValidTicketType
    private TicketType ticketType;
    private Long eventId;
    private String username;

    private String generateTicketCode() {
        return "TICKET-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}