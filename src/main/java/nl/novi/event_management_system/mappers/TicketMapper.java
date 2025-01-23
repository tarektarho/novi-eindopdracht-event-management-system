package nl.novi.event_management_system.mappers;

import nl.novi.event_management_system.dtos.TicketDTO;
import nl.novi.event_management_system.models.Ticket;

public class TicketMapper {

    // Convert from Entity to DTO
    public static TicketDTO toDTO(Ticket ticket) {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(ticket.getId());
        ticketDTO.setDate(ticket.getDate());
        ticketDTO.setQrCode(ticket.getQrCode());
        ticketDTO.setStatus(ticket.getStatus());
        ticketDTO.setUsername(ticket.getUser().getUsername());
        ticketDTO.setEventId(ticket.getEvent().getId());
        return ticketDTO;
    }

    // Convert from DTO to Entity
    public static Ticket toEntity(TicketDTO ticketDTO) {
        Ticket ticket = new Ticket();
        ticket.setId(ticketDTO.getId());
        ticket.setDate(ticketDTO.getDate());
        ticket.setQrCode(ticketDTO.getQrCode());
        ticket.setStatus(ticketDTO.getStatus());

        if (ticketDTO.getUsername() != null) {
            ticket.getUser().setUsername(ticketDTO.getUsername());
        }

        if (ticketDTO.getEventId() != 0) {
            ticket.getEvent().setId(ticketDTO.getEventId());
        }


        return ticket;
    }
}
