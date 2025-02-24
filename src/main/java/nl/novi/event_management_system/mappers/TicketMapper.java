package nl.novi.event_management_system.mappers;

import jakarta.validation.Valid;
import nl.novi.event_management_system.dtos.ticketDtos.TicketCreateDTO;
import nl.novi.event_management_system.dtos.ticketDtos.TicketResponseDTO;
import nl.novi.event_management_system.models.Ticket;

import java.util.List;
import java.util.stream.Collectors;

public class TicketMapper {

    /**
     * Maps a Ticket object to a TicketResponseDTO object
     *
     * @param ticket Ticket
     * @return TicketResponseDTO
     */
    public static TicketResponseDTO toResponseDTO(Ticket ticket) {
        if (ticket == null) {
            return null;
        }

        return TicketResponseDTO.builder()
                .id(ticket.getId())
                .ticketCode(ticket.getTicketCode())
                .ticketType(ticket.getTicketType())
                .purchaseDate(ticket.getPurchaseDate())
                .price(ticket.getPrice())
                .user(ticket.getUser() != null ? UserMapper.toUserProfileResponseDTO(ticket.getUser()) : null)
                .event(ticket.getEvent() != null ? EventMapper.toResponseDTO(ticket.getEvent()) : null)
                .build();
    }

    /**
     * Maps a list of Ticket objects to a list of TicketResponseDTO objects
     *
     * @param tickets List of Ticket
     * @return List of TicketResponseDTO
     */
    public static List<TicketResponseDTO> toResponseDTOList(List<Ticket> tickets) {
        return tickets.stream()
                .map(TicketMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Maps a TicketCreateDTO object to a Ticket object
     *
     * @param ticketCreateDTO TicketCreateDTO
     * @return Ticket
     */
    public static Ticket toEntity(@Valid TicketCreateDTO ticketCreateDTO) {
        Ticket ticket = new Ticket();
        ticket.setPrice(ticketCreateDTO.getPrice());
        ticket.setPurchaseDate(ticketCreateDTO.getPurchaseDate());
        ticket.setTicketType(ticketCreateDTO.getTicketType());

        return ticket;
    }
}
