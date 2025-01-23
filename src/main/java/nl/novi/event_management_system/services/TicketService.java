package nl.novi.event_management_system.services;

import nl.novi.event_management_system.dtos.TicketDTO;
import nl.novi.event_management_system.mappers.TicketMapper;
import nl.novi.event_management_system.models.Ticket;
import nl.novi.event_management_system.repositories.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public TicketDTO createTicket(TicketDTO ticketDTO) {
        Ticket ticket = TicketMapper.toEntity(ticketDTO);
        return TicketMapper.toDTO(ticketRepository.save(ticket));
    }

    public TicketDTO getTicketById(long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow();
        return TicketMapper.toDTO(ticket);
    }

    public void deleteTicket(long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new RuntimeException("Ticket not found for id: " + id));
        ticketRepository.delete(ticket);
    }

    public List<Ticket> getTicketsByUser(String username) {
        return ticketRepository.findTicketByUserUsername(username);
    }

    public List<Ticket> getTicketsByEvent(long eventId) {
        return ticketRepository.findTicketByEventId(eventId);
    }

    public Ticket updateTicket(long id, TicketDTO ticketDTO) {
        Ticket currentTicket = ticketRepository.findById(id).orElseThrow(() -> new RuntimeException("Ticket not found for id: " + id));
        Ticket updateTicket = TicketMapper.toEntity(ticketDTO);
        updateTicket.setId(currentTicket.getId());
        return ticketRepository.save(updateTicket);
    }

}
