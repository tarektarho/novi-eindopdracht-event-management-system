package nl.novi.event_management_system.mappers;

import jakarta.validation.Valid;
import nl.novi.event_management_system.dtos.ticketDtos.TicketCreateDTO;
import nl.novi.event_management_system.dtos.ticketDtos.TicketResponseDTO;
import nl.novi.event_management_system.models.Ticket;

import java.util.List;
import java.util.stream.Collectors;

public class TicketMapper {

    public static TicketResponseDTO toResponseDTO(Ticket ticket) {
        if(ticket == null) {
            return null;
        }

        TicketResponseDTO ticketResponseDTO = new TicketResponseDTO();
        ticketResponseDTO.setId(ticket.getId());
        ticketResponseDTO.setTicketCode(ticket.getTicketCode());
        ticketResponseDTO.setTicketType(ticket.getTicketType());
        ticketResponseDTO.setPurchaseDate(ticket.getPurchaseDate());
        ticketResponseDTO.setPrice(ticket.getPrice());

        if(ticket.getUser() != null) {
            ticketResponseDTO.setUser(UserMapper.toUserProfileResponseDTO(ticket.getUser()));
        }

        if(ticket.getEvent() != null) {
            ticketResponseDTO.setEvent(EventMapper.toResponseDTO(ticket.getEvent()));
        }

        return ticketResponseDTO;
    }

    public static List<TicketResponseDTO> toResponseDTOList(List<Ticket> tickets) {
        return tickets.stream()
                .map(TicketMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public static Ticket toEntity(@Valid TicketCreateDTO ticketCreateDTO) {
        Ticket ticket = new Ticket();
        ticket.setPrice(ticketCreateDTO.getPrice());
        ticket.setPurchaseDate(ticketCreateDTO.getPurchaseDate());
        ticket.setTicketType(ticketCreateDTO.getTicketType());
        ticket.setTicketCode(ticketCreateDTO.getTicketCode());


        return ticket;
    }
}
