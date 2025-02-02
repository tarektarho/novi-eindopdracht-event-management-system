package nl.novi.event_management_system.services;

import jakarta.validation.Valid;
import nl.novi.event_management_system.dtos.ticketDtos.TicketCreateDTO;
import nl.novi.event_management_system.dtos.ticketDtos.TicketResponseDTO;
import nl.novi.event_management_system.exceptions.RecordNotFoundException;
import nl.novi.event_management_system.mappers.TicketMapper;
import nl.novi.event_management_system.models.*;
import nl.novi.event_management_system.repositories.TicketRepository;
import nl.novi.event_management_system.repositories.UserRepository;
import nl.novi.event_management_system.utils.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserRepository userRepository;


    public TicketResponseDTO createTicket(@Valid TicketCreateDTO ticketCreateDTO) {
        ticketCreateDTO.setTicketCode(RandomStringGenerator.generateAlphaNumeric(20));
        Ticket ticket = TicketMapper.toEntity(ticketCreateDTO);

        if (ticketCreateDTO.getUsername() != null) {
            User user = userRepository.findByUsername(ticketCreateDTO.getUsername())
                    .orElseThrow(() -> new RecordNotFoundException("User not found."));
            ticket.setUser(user);
        }

        return TicketMapper.toResponseDTO(ticketRepository.save(ticket));
    }

    public List<TicketResponseDTO> getTickets() {
        return TicketMapper.toResponseDTOList(ticketRepository.findAll());
    }

    public TicketResponseDTO getTicketById(UUID id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Ticket not found."));
        return TicketMapper.toResponseDTO(ticket);
    }

    public TicketResponseDTO updateTicket(UUID id, TicketCreateDTO ticketCreateDTO) {
        if (!ticketRepository.existsById(id)) throw new RecordNotFoundException("Ticket not found.");
        Ticket storedTicket = ticketRepository.findById(id).orElseThrow(RecordNotFoundException::new);
        Ticket updatedTicket = TicketMapper.toEntity(ticketCreateDTO);
        updatedTicket.setId(storedTicket.getId());
        updatedTicket.setTicketCode(RandomStringGenerator.generateAlphaNumeric(20));
        Ticket savedTicket = ticketRepository.save(updatedTicket);
        return TicketMapper.toResponseDTO(savedTicket);
    }

    public boolean deleteTicketById(UUID id) {
        ticketRepository.findById(id)
                .orElseThrow(RecordNotFoundException::new);
        if (ticketRepository.existsById(id)) {
            ticketRepository.deleteById(id);
            return true;
        }
        return false;

    }

    //Todo double check if needed
    public List<Ticket> getTicketsForUser(String username) {
        return ticketRepository.findByUserUsername(username);
    }

    //Todo double check if needed
    public List<Ticket> getTicketsForEvent(UUID eventId) {
        return ticketRepository.findByEventId(eventId);
    }

    //assignUserToTicket
    //assignEventToTicket
}
