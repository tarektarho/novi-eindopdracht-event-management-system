package nl.novi.event_management_system.dtos.eventDtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class EventTicketIdDTO {
    @NotNull(message = "Ticket ID is mandatory")
    private UUID ticketId;

    public EventTicketIdDTO() {
    }

    public EventTicketIdDTO(UUID ticketId) {
        this.ticketId = ticketId;
    }

    public UUID getTicketId() {
        return ticketId;
    }

    public void setTicketId(UUID ticketId) {
        this.ticketId = ticketId;
    }
}
