package nl.novi.event_management_system.services;

import jakarta.validation.Valid;
import nl.novi.event_management_system.dtos.ticketDtos.TicketCreateDTO;
import nl.novi.event_management_system.dtos.ticketDtos.TicketResponseDTO;
import nl.novi.event_management_system.exceptions.EventNotFoundException;
import nl.novi.event_management_system.exceptions.RecordNotFoundException;
import nl.novi.event_management_system.exceptions.UsernameNotFoundException;
import nl.novi.event_management_system.mappers.TicketMapper;
import nl.novi.event_management_system.models.*;
import nl.novi.event_management_system.repositories.EventRepository;
import nl.novi.event_management_system.repositories.TicketRepository;
import nl.novi.event_management_system.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * This class contains the business logic for the Ticket entity.
 */
@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    /**
     * Constructor for the TicketService class.
     *
     * @param ticketRepository The repository for the Ticket entity.
     */
    public TicketService(TicketRepository ticketRepository, UserRepository userRepository, EventRepository eventRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    /**
     * Creates a new ticket.
     *
     * @param ticketCreateDTO The DTO containing the information for the ticket.
     * @return The DTO containing the information for the created ticket.
     */
    public TicketResponseDTO createTicket(@Valid TicketCreateDTO ticketCreateDTO) {
        User user = userRepository.findByUsername(ticketCreateDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        Event event = eventRepository.findById(ticketCreateDTO.getEventId())
                .orElseThrow(() -> new EventNotFoundException(ticketCreateDTO.getEventId()));

        Ticket ticket = TicketMapper.toEntity(ticketCreateDTO);

        ticket.setUser(user);
        ticket.setEvent(event);

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
     * @param ticketCreateDTO The DTO containing the updated ticket details.
     * @return The DTO containing the information for the updated ticket.
     */
    public TicketResponseDTO updateTicket(UUID id, TicketCreateDTO ticketCreateDTO) {
        // Fetch the existing ticket (throws exception if not found)
        Ticket storedTicket = ticketRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Ticket not found with ID: " + id));

        // Fetch the user only if username is provided
        User user = null;
        if (ticketCreateDTO.getUsername() != null) {
            user = userRepository.findByUsername(ticketCreateDTO.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + ticketCreateDTO.getUsername()));
        }

        Event event = eventRepository.findById(ticketCreateDTO.getEventId())
                .orElseThrow(() -> new EventNotFoundException("Event not found: " + ticketCreateDTO.getEventId()));

        // Update fields in the existing ticket
        storedTicket.setPrice(ticketCreateDTO.getPrice());
        storedTicket.setTicketType(ticketCreateDTO.getTicketType());
        storedTicket.setPurchaseDate(ticketCreateDTO.getPurchaseDate());
        storedTicket.setEvent(event);

        // Set user only if it's provided
        if (user != null) {
            storedTicket.setUser(user);
        }

        // Save updated ticket and return response DTO
        Ticket updatedTicket = ticketRepository.save(storedTicket);
        return TicketMapper.toResponseDTO(updatedTicket);
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
