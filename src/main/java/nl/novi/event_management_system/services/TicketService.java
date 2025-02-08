package nl.novi.event_management_system.services;

import jakarta.validation.Valid;
import nl.novi.event_management_system.dtos.ticketDtos.TicketCreateDTO;
import nl.novi.event_management_system.dtos.ticketDtos.TicketResponseDTO;
import nl.novi.event_management_system.exceptions.RecordNotFoundException;
import nl.novi.event_management_system.mappers.TicketMapper;
import nl.novi.event_management_system.models.*;
import nl.novi.event_management_system.repositories.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * This class contains the business logic for the Ticket entity.
 */
@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    /**
     * Constructor for the TicketService class.
     *
     * @param ticketRepository The repository for the Ticket entity.
     */
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    /**
     * Creates a new ticket.
     *
     * @param ticketCreateDTO The DTO containing the information for the ticket.
     * @return The DTO containing the information for the created ticket.
     */
    public TicketResponseDTO createTicket(@Valid TicketCreateDTO ticketCreateDTO) {
        Ticket ticket = TicketMapper.toEntity(ticketCreateDTO);
        return TicketMapper.toResponseDTO(ticketRepository.save(ticket));
    }

    /**
     * Retrieves all tickets.
     *
     * @return A list of DTOs containing the information for all tickets.
     */
    public List<TicketResponseDTO> getTickets() {
        return TicketMapper.toResponseDTOList(ticketRepository.findAll());
    }

    /**
     * Retrieves a ticket by its ID.
     *
     * @param id The ID of the ticket to retrieve.
     * @return The DTO containing the information for the ticket.
     */
    public TicketResponseDTO getTicketById(UUID id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Ticket not found."));
        return TicketMapper.toResponseDTO(ticket);
    }

    /**
     * Updates a ticket.
     *
     * @param id              The ID of the ticket to update.
     * @param ticketCreateDTO The DTO containing the information for the updated ticket.
     * @return The DTO containing the information for the updated ticket.
     */
    public TicketResponseDTO updateTicket(UUID id, TicketCreateDTO ticketCreateDTO) {
        if (!ticketRepository.existsById(id)) throw new RecordNotFoundException("Ticket not found.");
        Ticket storedTicket = ticketRepository.findById(id).orElseThrow(RecordNotFoundException::new);
        Ticket updatedTicket = TicketMapper.toEntity(ticketCreateDTO);
        updatedTicket.setId(storedTicket.getId());
        Ticket savedTicket = ticketRepository.save(updatedTicket);
        return TicketMapper.toResponseDTO(savedTicket);
    }

    /**
     * Deletes a ticket by its ID.
     *
     * @param id The ID of the ticket to delete.
     * @return True if the ticket was deleted, false if the ticket was not found.
     */
    public boolean deleteTicketById(UUID id) {
        ticketRepository.findById(id)
                .orElseThrow(RecordNotFoundException::new);
        if (ticketRepository.existsById(id)) {
            ticketRepository.deleteById(id);
            return true;
        }
        return false;

    }
}
