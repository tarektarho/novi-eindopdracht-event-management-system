package nl.novi.event_management_system.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.novi.event_management_system.dtos.eventDtos.EventCreateDTO;
import nl.novi.event_management_system.dtos.eventDtos.EventResponseDTO;
import nl.novi.event_management_system.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Tag(name = "Event API", description = "Event related endpoints")
@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/create")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<?> createEvent(@Valid @RequestBody EventCreateDTO eventCreateDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            EventResponseDTO newEventDTO = eventService.createEvent(eventCreateDTO);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(newEventDTO.getId())
                    .toUri();
            return ResponseEntity.created(location).body(newEventDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating event: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public EventResponseDTO getEventById(@PathVariable UUID id) {
        return eventService.findEventById(id);
    }

    @GetMapping("/organizer/{username}")
    public List<EventResponseDTO> getEventsByOrganizer(@PathVariable String username) {
        return eventService.getEventsByOrganizer(username);
    }

    @GetMapping("/all")
    public List<EventResponseDTO> getAllEvents() {
        return eventService.getAllEvents();
    }

    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public EventResponseDTO updateEvent(@PathVariable UUID id, @Valid @RequestBody EventCreateDTO eventCreateDTO) {
        return eventService.updateEvent(id, eventCreateDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID id) {
        boolean isDeleted = eventService.deleteEventById(id);

        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/user/{username}/register")
    public ResponseEntity<Void> registerUserForEvent(@PathVariable UUID id, @PathVariable String username) {
        eventService.registerUserForEvent(id, username);
        return ResponseEntity.noContent().build();
    }
}
