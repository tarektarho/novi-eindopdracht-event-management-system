package nl.novi.event_management_system.dtos.ticketDtos;

import lombok.Getter;
import lombok.Setter;
import nl.novi.event_management_system.dtos.eventDtos.EventResponseDTO;
import nl.novi.event_management_system.dtos.userDtos.UserResponseDTO;
import nl.novi.event_management_system.enums.TicketType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class TicketResponseDTO {
    private UUID id;
    private BigDecimal price;
    private String ticketCode;
    private LocalDateTime purchaseDate;
    private TicketType ticketType;
    private UserResponseDTO user;
    private EventResponseDTO event;
}
