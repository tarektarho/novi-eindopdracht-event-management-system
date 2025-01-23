package nl.novi.event_management_system.controllers;

import jakarta.validation.Valid;
import nl.novi.event_management_system.dtos.eventDtos.EventCreateDTO;
import nl.novi.event_management_system.dtos.eventDtos.EventResponseDTO;
import nl.novi.event_management_system.mappers.EventMapper;
import nl.novi.event_management_system.services.EventService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getEvents() {
        List<EventResponseDTO> events;

        try {
            events = EventMapper.toEventResponseDTOList(eventService.getEvents());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable long id) {
        EventResponseDTO responseDTO;

        try {
            responseDTO = eventService.getEventById(id);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping
    public ResponseEntity<Object> createEvent(@Valid @RequestBody EventCreateDTO eventCreateDTO, BindingResult result) {
        if (result.hasErrors()) {
            // Collect all the error messages and return them in the response
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(errorMessages); // Send 400 with error messages
        }

        try {
            // Call the service layer to handle event creation
            EventResponseDTO eventResponseDTO = eventService.createEvent(eventCreateDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(eventResponseDTO);
        } catch (IllegalArgumentException e) {
            // Handle custom validation issues
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            // Handle database constraint violations
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid organizer: Referenced user ID does not exist in the database.");
        } catch (Exception e) {
            // Catch-all for unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please try again later.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable long id, @Valid @RequestBody EventCreateDTO eventCreateDTO) {
        EventResponseDTO responseDTO;

        try {
            responseDTO = eventService.updateEvent(id, eventCreateDTO);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable long id) {
        try {
            eventService.deleteEvent(id);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
