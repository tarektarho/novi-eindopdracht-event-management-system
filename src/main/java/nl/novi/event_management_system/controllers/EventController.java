package nl.novi.event_management_system.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import nl.novi.event_management_system.dtos.eventDtos.*;
import nl.novi.event_management_system.services.EventService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
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

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createEvent(@Valid @RequestBody EventCreateDTO eventCreateDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }

        EventResponseDTO newEventDTO = eventService.createEvent(eventCreateDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newEventDTO.getId())
                .toUri();
        return ResponseEntity.created(location).body(newEventDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable UUID id) {
        return ResponseEntity.ok(eventService.findEventById(id));
    }

    @GetMapping("/organizer/{username}")
    public ResponseEntity<List<EventResponseDTO>> getEventsByOrganizer(@PathVariable String username) {
        return ResponseEntity.ok(eventService.getEventsByOrganizer(username));
    }

    @GetMapping("/all")
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable UUID id, @Valid @RequestBody EventCreateDTO eventCreateDTO) {
        return ResponseEntity.ok(eventService.updateEvent(id, eventCreateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID id) {
        boolean isDeleted = eventService.deleteEventById(id);

        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{eventId}/organizer/assign")
    public ResponseEntity<String> assignOrganizerToEvent(
            @PathVariable UUID eventId,
            @RequestBody EventOrganizerUsernameDTO organizerUsername) {
        eventService.assignOrganizerToEvent(eventId, organizerUsername.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{eventId}/organizer/remove")
    public ResponseEntity<String> removeOrganizerFromEvent(@PathVariable UUID eventId, @RequestBody EventOrganizerUsernameDTO organizerUsername) {
        eventService.removeOrganizerFromEvent(eventId, organizerUsername.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{eventId}/participants/assign")
    public ResponseEntity<String> assignParticipantToEvent(
            @PathVariable UUID eventId,
            @RequestBody EventParticipantUsernameWrapperDTO wrapper) {

        eventService.assignParticipantToEvent(eventId, wrapper.getParticipants());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{eventId}/participants/remove")
    public ResponseEntity<Map<String, Object>> removeParticipantsFromEvent(
            @PathVariable UUID eventId,
            @RequestBody EventParticipantUsernameWrapperDTO wrapper) {

        List<String> removedParticipants = new ArrayList<>();

        wrapper.getParticipants().forEach(participant -> {
            eventService.removeParticipantFromEvent(eventId, participant);
            removedParticipants.add(participant.getUsername());
        });

        // Construct response
        Map<String, Object> response = new HashMap<>();
        response.put("removed", removedParticipants);

        log.info("All participants removed successfully from event '{}'", eventId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{eventId}/tickets/add")
    public ResponseEntity<?> addTicketsToEvent(
            @PathVariable UUID eventId,
            @RequestBody EventTicketIdsWrapperDTO wrapper) {

        eventService.addTicketsToEvent(eventId, wrapper.getTicketIds());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{eventId}/tickets/remove")
    public ResponseEntity<Map<String, Object>> removeTicketsFromEvent(
            @PathVariable UUID eventId,
            @RequestBody EventTicketIdsWrapperDTO wrapper) {

        List<String> removedTickets = new ArrayList<>();

        wrapper.getTicketIds().forEach(ticket -> {
            eventService.removeTicketFromEvent(eventId, ticket);
            removedTickets.add(ticket.getTicketId().toString());
        });

        // Construct response
        Map<String, Object> response = new HashMap<>();
        response.put("removed", removedTickets);
        return ResponseEntity.ok(response);

    }

    @PatchMapping("/{eventId}/feedback/add")
    public ResponseEntity<?> addFeedbackToEvent(
            @PathVariable UUID eventId,
            @RequestBody EventFeedbackIdWrapperDTO eventFeedbackIdWrapperDTO) {

        eventService.AddFeedbacksToEvent(eventId, eventFeedbackIdWrapperDTO.getFeedbackIds());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{eventId}/feedback/remove")
    public ResponseEntity<Map<String, Object>> removeFeedbackFromEvent(
            @PathVariable UUID eventId,
            @RequestBody EventFeedbackIdWrapperDTO eventFeedbackIdWrapperDTO) {

        List<String> removedFeedbacks = new ArrayList<>();

        eventFeedbackIdWrapperDTO.getFeedbackIds().forEach(feedback -> {
            eventService.removeFeedbackFromEvent(eventId, feedback);
            removedFeedbacks.add(feedback.getFeedbackId().toString());
        });

        Map<String, Object> response = new HashMap<>();
        response.put("removed", removedFeedbacks);

        return ResponseEntity.ok(response);
    }
}
