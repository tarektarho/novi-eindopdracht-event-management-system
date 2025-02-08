package nl.novi.event_management_system.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.novi.event_management_system.dtos.ticketDtos.TicketCreateDTO;
import nl.novi.event_management_system.dtos.ticketDtos.TicketResponseDTO;
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

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Create a new ticket
     *
     * @param ticketCreateDTO TicketCreateDTO
     * @return ResponseEntity<?>
     */
    @PostMapping("/create")
    public ResponseEntity<?> createTicket(@Valid @RequestBody TicketCreateDTO ticketCreateDTO, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errorMessages = new StringBuilder();
            result.getAllErrors().forEach(error -> errorMessages.append(error.getDefaultMessage()).append(" "));
            return ResponseEntity.badRequest().body(errorMessages.toString());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.createTicket(ticketCreateDTO));
    }

    /**
     * Get all tickets
     *
     * @return List<TicketResponseDTO>
     */
    @GetMapping("/all")
    public List<TicketResponseDTO> getTickets() {
        return ticketService.getTickets();
    }

    /**
     * Get ticket by ID
     *
     * @param id UUID
     * @return ResponseEntity<TicketResponseDTO>
     */
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> getTicketById(@PathVariable UUID id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    /**
     * Update ticket by ID
     *
     * @param id              UUID
     * @param ticketCreateDTO TicketCreateDTO
     * @return ResponseEntity<TicketResponseDTO>
     */
    @PutMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> updateTicket(@PathVariable UUID id, @Valid @RequestBody TicketCreateDTO ticketCreateDTO) {
        return ResponseEntity.ok(ticketService.updateTicket(id, ticketCreateDTO));
    }

    /**
     * Delete ticket by ID
     *
     * @param id UUID
     * @return ResponseEntity<Void>
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable UUID id) {
        boolean isDeleted = ticketService.deleteTicketById(id);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
