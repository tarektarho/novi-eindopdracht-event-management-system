package nl.novi.event_management_system.dtos.ticketDtos;

import lombok.Builder;
import lombok.Data;
import nl.novi.event_management_system.dtos.eventDtos.EventResponseDTO;
import nl.novi.event_management_system.dtos.userDtos.UserProfileDTO;
import nl.novi.event_management_system.enums.TicketType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Builder
@Data
public class TicketResponseDTO {
    private UUID id;
    private BigDecimal price;
    private String ticketCode;
    private LocalDate purchaseDate;
    private TicketType ticketType;
    private UserProfileDTO user;
    private EventResponseDTO event;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TicketResponseDTO that = (TicketResponseDTO) obj;
        return Objects.equals(id, that.id) &&
                Objects.equals(event, that.event) &&
                Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, event, price);
    }
}
