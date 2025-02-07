package nl.novi.event_management_system.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import nl.novi.event_management_system.dtos.eventDtos.*;
import nl.novi.event_management_system.exceptions.UsernameNotFoundException;
import nl.novi.event_management_system.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@Slf4j
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

    @PatchMapping("/{eventId}/assign-participants")
    public ResponseEntity<String> assignParticipantToEvent(
            @PathVariable UUID eventId,
            @RequestBody EventParticipantUsernameWrapperDTO wrapper) {

        try {
            eventService.assignParticipantToEvent(eventId, wrapper.getParticipants());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{eventId}/remove-participants")
    public ResponseEntity<Map<String, Object>> removeParticipantsFromEvent(
            @PathVariable UUID eventId,
            @RequestBody EventParticipantUsernameWrapperDTO wrapper) {

        List<String> removedParticipants = new ArrayList<>();
        Map<String, String> failedParticipants = new HashMap<>();

        wrapper.getParticipants().forEach(participant -> {
            try {
                eventService.removeParticipantFromEvent(eventId, participant);
                removedParticipants.add(participant.getUsername());
            } catch (UsernameNotFoundException e) {
                failedParticipants.put(participant.getUsername(), e.getMessage());
            } catch (Exception e) {
                failedParticipants.put(participant.getUsername(), "Unexpected error occurred.");
            }
        });

        // Construct response
        Map<String, Object> response = new HashMap<>();
        response.put("removed", removedParticipants);
        response.put("failed", failedParticipants);

        if (failedParticipants.isEmpty()) {
            log.info("All participants removed successfully from event '{}'", eventId);
            return ResponseEntity.ok(response);
        } else {
            log.warn("Some participants could not be removed from event '{}': {}", eventId, failedParticipants);
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
        }
    }

    @PatchMapping("/{eventId}/add-tickets")
    public ResponseEntity<?> addTicketsToEvent(
            @PathVariable UUID eventId,
            @RequestBody EventTicketIdsWrapperDTO wrapper) {

        try {
            eventService.addTicketsToEvent(eventId, wrapper.getTicketIds());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{eventId}/remove-tickets")
    public ResponseEntity<Map<String, Object>> removeTicketsFromEvent(
            @PathVariable UUID eventId,
            @RequestBody EventTicketIdsWrapperDTO wrapper) {

        List<String> removedTickets = new ArrayList<>();
        Map<String, String> failedTickets = new HashMap<>();

        wrapper.getTicketIds().forEach(ticket -> {
            try {
                eventService.removeTicketFromEvent(eventId, ticket);
                removedTickets.add(ticket.getTicketId().toString());
            } catch (UsernameNotFoundException e) {
                failedTickets.put(ticket.getTicketId().toString(), e.getMessage());
            } catch (Exception e) {
                failedTickets.put(ticket.getTicketId().toString(), "Unexpected error occurred.");
            }
        });

        // Construct response
        Map<String, Object> response = new HashMap<>();
        response.put("removed", removedTickets);
        response.put("failed", failedTickets);

        if (failedTickets.isEmpty()) {
            log.info("All tickets removed successfully from event '{}'", eventId);
            return ResponseEntity.ok(response);
        } else {
            log.warn("Some tickets could not be removed from event '{}': {}", eventId, failedTickets);
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
        }
    }

    @PatchMapping("/{eventId}/add-feedback")
    public ResponseEntity<?> addFeedbackToEvent(
            @PathVariable UUID eventId,
            @RequestBody EventFeedbackIdWrapperDTO eventFeedbackIdWrapperDTO) {

        try {
            eventService.AddFeedbacksToEvent(eventId, eventFeedbackIdWrapperDTO.getFeedbackIds());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{eventId}/remove-feedback")
    public ResponseEntity<Map<String, Object>> removeFeedbackFromEvent(
            @PathVariable UUID eventId,
            @RequestBody EventFeedbackIdWrapperDTO eventFeedbackIdWrapperDTO) {

        List<String> removedFeedbacks = new ArrayList<>();
        Map<String, String> failedFeedbacks = new HashMap<>();

        eventFeedbackIdWrapperDTO.getFeedbackIds().forEach(feedback -> {
            try {
                eventService.removeFeedbackFromEvent(eventId, feedback);
                removedFeedbacks.add(feedback.getFeedbackId().toString());
            } catch (UsernameNotFoundException e) {
                failedFeedbacks.put(feedback.getFeedbackId().toString(), e.getMessage());
            } catch (Exception e) {
                failedFeedbacks.put(feedback.getFeedbackId().toString(), "Unexpected error occurred.");
            }
        });

        Map<String, Object> response = new HashMap<>();
        response.put("removed", removedFeedbacks);
        response.put("failed", failedFeedbacks);

        if (failedFeedbacks.isEmpty()) {
            log.info("All feedbacks removed successfully from event '{}'", eventId);
            return ResponseEntity.ok(response);
        } else {
            log.warn("Some feedbacks could not be removed from event '{}': {}", eventId, failedFeedbacks);
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
        }
    }

}
