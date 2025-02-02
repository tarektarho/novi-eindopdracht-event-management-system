package nl.novi.event_management_system.dtos.ticketDtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import nl.novi.event_management_system.dtos.eventDtos.EventResponseDTO;
import nl.novi.event_management_system.dtos.userDtos.UserProfileDTO;
import nl.novi.event_management_system.enums.TicketType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)  // This will exclude null fields from the response
public class TicketResponseDTO {
    private UUID id;
    private BigDecimal price;
    private String ticketCode;
    private LocalDateTime purchaseDate;
    private TicketType ticketType;
    private UserProfileDTO user;
    private EventResponseDTO event;
}
