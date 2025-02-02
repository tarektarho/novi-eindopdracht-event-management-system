package nl.novi.event_management_system.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import nl.novi.event_management_system.dtos.ticketDtos.TicketCreateDTO;
import nl.novi.event_management_system.dtos.ticketDtos.TicketResponseDTO;
import nl.novi.event_management_system.models.Ticket;
import nl.novi.event_management_system.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Ticket API", description = "Ticket related endpoints")
@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/create")
    public ResponseEntity<TicketResponseDTO> createTicket(@Valid @RequestBody TicketCreateDTO ticketCreateDTO, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errorMessages = new StringBuilder();
            result.getAllErrors().forEach(error -> errorMessages.append(error.getDefaultMessage()).append(" "));
            throw new ValidationException("Validation failed: " + errorMessages);
        }


        return ResponseEntity.status(201).body(ticketService.createTicket(ticketCreateDTO));
    }

    @GetMapping("/all")
    public List<TicketResponseDTO> getTickets() {
        return ticketService.getTickets();
    }

    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public TicketResponseDTO getTicketById(@PathVariable UUID id) {
        return ticketService.getTicketById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public TicketResponseDTO updateTicket(@PathVariable UUID id, @Valid @RequestBody TicketCreateDTO ticketCreateDTO) {
        return ticketService.updateTicket(id, ticketCreateDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable UUID id) {
        boolean isDeleted = ticketService.deleteTicketById(id);

        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{username}")
    public List<Ticket> getTicketsForUser(@PathVariable String username) {
        return ticketService.getTicketsForUser(username);
    }

    @GetMapping("/event/{eventId}")
    public List<Ticket> getTicketsForEvent(@PathVariable UUID eventId) {
        return ticketService.getTicketsForEvent(eventId);
    }
}
