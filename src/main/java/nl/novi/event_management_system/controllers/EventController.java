package nl.novi.event_management_system.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.novi.event_management_system.dtos.eventDtos.*;
import nl.novi.event_management_system.services.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@Tag(name = "Event API", description = "Event related endpoints")
@RestController
@RequestMapping("/api/v1/events")
public class EventController {
    Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Create a new event
     *
     * @param eventCreateDTO EventCreateDTO
     * @return ResponseEntity<?>
     */
    @PostMapping()
    @Operation(
            summary = "Create a new event",
            description = "Allows an admin or organizer to create a new event by providing event details."
    )    public ResponseEntity<Object> createEvent(@Valid @RequestBody EventCreateDTO eventCreateDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            log.error("Error creating event: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        EventResponseDTO newEventDTO = eventService.createEvent(eventCreateDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newEventDTO.getId())
                .toUri();
        return ResponseEntity.created(location).body(newEventDTO);
    }

    /**
     * Get event by ID
     *
     * @param id UUID
     * @return ResponseEntity<EventResponseDTO>
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable UUID id) {
        return ResponseEntity.ok(eventService.findEventById(id));
    }

    /**
     * Get events by organizer
     *
     * @param username String
     * @return ResponseEntity<List < EventResponseDTO>>
     */
    @GetMapping("/organizer/{username}")
    @Operation(
            summary = "Get events by organizer",
            description = "Allows an admin or organizer to get all events created by a specific organizer."
    )
    public ResponseEntity<List<EventResponseDTO>> getEventsByOrganizer(@PathVariable String username) {
        return ResponseEntity.ok(eventService.getEventsByOrganizer(username));
    }

    /**
     * Get all events
     *
     * @return ResponseEntity<List < EventResponseDTO>>
     */
    @GetMapping()
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    /**
     * Update event by ID
     *
     * @param id             UUID
     * @param eventCreateDTO EventCreateDTO
     * @return ResponseEntity<EventResponseDTO>
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable UUID id, @Valid @RequestBody EventCreateDTO eventCreateDTO) {
        return ResponseEntity.ok(eventService.updateEvent(id, eventCreateDTO));
    }

    /**
     * Delete event by ID
     *
     * @param id UUID
     * @return ResponseEntity<Void>
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID id) {
        boolean isDeleted = eventService.deleteEventById(id);

        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Assign organizer to event
     *
     * @param id           UUID
     * @param organizerUsername EventOrganizerUsernameDTO
     * @return ResponseEntity<String>
     */
    @PostMapping("/{id}/organizer")
    @Operation(
            summary = "Assign organizer to event",
            description = "Allows an admin to assign an organizer to an event by providing the event ID and organizer username."
    )
    public ResponseEntity<String> assignOrganizerToEvent(
            @PathVariable UUID id,
            @RequestBody EventOrganizerUsernameDTO organizerUsername) {
        eventService.assignOrganizerToEvent(id, organizerUsername.getUsername());
        return ResponseEntity.noContent().build();
    }

    /**
     * Remove organizer from event
     *
     * @param id           UUID
     * @param organizerUsername EventOrganizerUsernameDTO
     * @return ResponseEntity<String>
     */
    @DeleteMapping("/{id}/organizer")
    @Operation(
            summary = "Remove organizer from event",
            description = "Allows an admin to remove an organizer from an event by providing the event ID and organizer username."
    )
    public ResponseEntity<String> removeOrganizerFromEvent(@PathVariable UUID id, @RequestBody EventOrganizerUsernameDTO organizerUsername) {
        eventService.removeOrganizerFromEvent(id, organizerUsername.getUsername());
        return ResponseEntity.noContent().build();
    }

    /**
     * Assign participant to event
     *
     * @param id UUID
     * @param wrapper EventParticipantUsernameWrapperDTO
     * @return ResponseEntity<String>
     */
    @PostMapping("/{id}/participants")
    @Operation(
            summary = "Assign participant to event",
            description = "Allows an admin or organizer to assign participants to an event by providing the event ID and participant usernames."
    )
    public ResponseEntity<String> assignParticipantToEvent(
            @PathVariable UUID id,
            @RequestBody EventParticipantUsernameWrapperDTO wrapper) {

        eventService.assignParticipantToEvent(id, wrapper.getParticipants());
        return ResponseEntity.noContent().build();
    }

    /**
     * Remove participants from event
     *
     * @param id UUID
     * @param wrapper EventParticipantUsernameWrapperDTO
     * @return ResponseEntity<Map < String, Object>>
     */
    @DeleteMapping("/{id}/participants")
    @Operation(
            summary = "Remove participants from event",
            description = "Allows an admin or organizer to remove participants from an event by providing the event ID and participant usernames."
    )
    public ResponseEntity<Map<String, Object>> removeParticipantsFromEvent(
            @PathVariable UUID id,
            @RequestBody EventParticipantUsernameWrapperDTO wrapper) {

        List<String> removedParticipants = new ArrayList<>();

        wrapper.getParticipants().forEach(participant -> {
            eventService.removeParticipantFromEvent(id, participant);
            removedParticipants.add(participant.getUsername());
        });

        Map<String, Object> response = new HashMap<>();
        response.put("removed", removedParticipants);
        return ResponseEntity.ok(response);
    }

    /**
     * Add tickets to event
     *
     * @param id UUID
     * @param wrapper EventTicketIdsWrapperDTO
     * @return ResponseEntity<?>
     */
    @PostMapping("/{id}/tickets")
    @Operation(
            summary = "Add tickets to event",
            description = "Allows an admin or organizer to add tickets to an event by providing the event ID and ticket IDs."
    )
    public ResponseEntity<?> addTicketsToEvent(
            @PathVariable UUID id,
            @RequestBody EventTicketIdsWrapperDTO wrapper) {

        eventService.addTicketsToEvent(id, wrapper.getTicketIds());
        return ResponseEntity.noContent().build();
    }

    /**
     * Remove tickets from event
     *
     * @param id UUID
     * @param wrapper EventTicketIdsWrapperDTO
     * @return ResponseEntity<Map < String, Object>>
     */
    @DeleteMapping("/{id}/tickets")
    @Operation(
            summary = "Remove tickets from event",
            description = "Allows an admin or organizer to remove tickets from an event by providing the event ID and ticket IDs."
    )
    public ResponseEntity<Map<String, Object>> removeTicketsFromEvent(
            @PathVariable UUID id,
            @RequestBody EventTicketIdsWrapperDTO wrapper) {

        List<String> removedTickets = new ArrayList<>();

        wrapper.getTicketIds().forEach(ticket -> {
            eventService.removeTicketFromEvent(id, ticket);
            removedTickets.add(ticket.getTicketId().toString());
        });

        Map<String, Object> response = new HashMap<>();
        response.put("removed", removedTickets);
        return ResponseEntity.ok(response);

    }

    /**
     * Add feedbacks to event
     *
     * @param id                   UUID
     * @param eventFeedbackIdWrapperDTO EventFeedbackIdWrapperDTO
     * @return ResponseEntity<?>
     */
    @PostMapping("/{id}/feedback")
    @Operation(
            summary = "Add feedbacks to event",
            description = "Allows an admin or organizer to add feedbacks to an event by providing the event ID and feedback IDs."
    )
    public ResponseEntity<?> addFeedbackToEvent(
            @PathVariable UUID id,
            @RequestBody EventFeedbackIdWrapperDTO eventFeedbackIdWrapperDTO) {

        eventService.AddFeedbacksToEvent(id, eventFeedbackIdWrapperDTO.getFeedbackIds());
        return ResponseEntity.noContent().build();
    }

    /**
     * Remove feedbacks from event
     *
     * @param id                   UUID
     * @param eventFeedbackIdWrapperDTO EventFeedbackIdWrapperDTO
     * @return ResponseEntity<Map < String, Object>>
     */
    @DeleteMapping("/{id}/feedback")
    @Operation(
            summary = "Remove feedbacks from event",
            description = "Allows an admin or organizer to remove feedbacks from an event by providing the event ID and feedback IDs."
    )
    public ResponseEntity<Map<String, Object>> removeFeedbackFromEvent(
            @PathVariable UUID id,
            @RequestBody EventFeedbackIdWrapperDTO eventFeedbackIdWrapperDTO) {

        List<String> removedFeedbacks = new ArrayList<>();

        eventFeedbackIdWrapperDTO.getFeedbackIds().forEach(feedback -> {
            eventService.removeFeedbackFromEvent(id, feedback);
            removedFeedbacks.add(feedback.getFeedbackId().toString());
        });

        Map<String, Object> response = new HashMap<>();
        response.put("removed", removedFeedbacks);
        return ResponseEntity.ok(response);
    }
}
