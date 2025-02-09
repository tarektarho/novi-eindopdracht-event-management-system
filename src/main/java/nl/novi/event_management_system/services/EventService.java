package nl.novi.event_management_system.services;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import nl.novi.event_management_system.controllers.GlobalExceptionHandler;
import nl.novi.event_management_system.dtos.eventDtos.*;
import nl.novi.event_management_system.exceptions.EventNotFoundException;
import nl.novi.event_management_system.exceptions.RecordNotFoundException;
import nl.novi.event_management_system.exceptions.UsernameNotFoundException;
import nl.novi.event_management_system.mappers.EventMapper;
import nl.novi.event_management_system.models.Event;
import nl.novi.event_management_system.models.Feedback;
import nl.novi.event_management_system.models.Ticket;
import nl.novi.event_management_system.models.User;
import nl.novi.event_management_system.repositories.EventRepository;
import nl.novi.event_management_system.repositories.FeedbackRepository;
import nl.novi.event_management_system.repositories.TicketRepository;
import nl.novi.event_management_system.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * This class contains the business logic for the Event entity.
 */
@Service
public class EventService {
    String LOG_MESSAGE_EVENT_NOT_FOUND = "Event not found with ID: {}";
    Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;
    private final TicketRepository ticketRepository;

    /**
     * Constructor for the EventService class.
     *
     * @param eventRepository    The repository for the Event entity.
     * @param userRepository     The repository for the User entity.
     * @param feedbackRepository The repository for the Feedback entity.
     * @param ticketRepository   The repository for the Ticket entity.
     */
    public EventService(EventRepository eventRepository, UserRepository userRepository, FeedbackRepository feedbackRepository, TicketRepository ticketRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.feedbackRepository = feedbackRepository;
        this.ticketRepository = ticketRepository;
    }

    /**
     * Creates a new event.
     *
     * @param eventCreateDTO The DTO containing the information for the event.
     * @return The DTO containing the information for the created event.
     */
    public EventResponseDTO createEvent(@Valid EventCreateDTO eventCreateDTO) {
        Event event = EventMapper.toEntity(eventCreateDTO);
        eventRepository.save(event);
        return EventMapper.toResponseDTO(event);
    }

    /**
     * Retrieves an event by its ID.
     *
     * @param id The ID of the event to retrieve.
     * @return The DTO containing the information for the event.
     */
    public EventResponseDTO findEventById(UUID id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
        return EventMapper.toResponseDTO(event);
    }

    /**
     * Retrieves all events.
     *
     * @return A list of DTOs containing the information for all events.
     */
    public List<EventResponseDTO> getEventsByOrganizer(String username) {

        return EventMapper.toResponseDTOList(eventRepository.findByOrganizerUsername(username));
    }

    /**
     * Retrieves all events.
     *
     * @return A list of DTOs containing the information for all events.
     */
    public List<EventResponseDTO> getAllEvents() {
        return EventMapper.toResponseDTOList(eventRepository.findAll());
    }

    /**
     * Updates an event.
     *
     * @param id             The ID of the event to update.
     * @param eventCreateDTO The DTO containing the information for the updated event.
     * @return The DTO containing the information for the updated event.
     */
    public EventResponseDTO updateEvent(UUID id, EventCreateDTO eventCreateDTO) {
        if (!eventRepository.existsById(id)) throw new EventNotFoundException(id);
        Event storedEvent = eventRepository.findEventById(id).orElseThrow(() -> new EventNotFoundException(id));
        Event updatedEvent = EventMapper.toEntity(eventCreateDTO);
        updatedEvent.setId(storedEvent.getId());

        Event savedEvent = eventRepository.save(updatedEvent);

        return EventMapper.toResponseDTO(savedEvent);
    }

    /**
     * Deletes an event by its ID.
     *
     * @param id The ID of the event to delete.
     * @return True if the event was deleted, false if the event was not found.
     */
    public boolean deleteEventById(UUID id) {
        eventRepository.findEventById(id)
                .orElseThrow(() -> new EventNotFoundException(id));

        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Assigns an organizer to an event.
     *
     * @param eventId           The ID of the event to assign the organizer to.
     * @param organizerUsername The username of the organizer to assign to the event.
     */
    public void assignOrganizerToEvent(UUID eventId, String organizerUsername) {
        log.info("Assigning organizer '{}' to event '{}'", organizerUsername, eventId);

        Event event = eventRepository.findEventById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        User organizer = userRepository.findByUsername(organizerUsername)
                .orElseThrow(() -> new UsernameNotFoundException(organizerUsername));

        event.setOrganizer(organizer);
        eventRepository.save(event);
        log.info("Successfully assigned organizer '{}' to event '{}'", organizerUsername, eventId);
    }

    /**
     * Removes an organizer from an event.
     *
     * @param eventId           The ID of the event to remove the organizer from.
     * @param organizerUsername The username of the organizer to remove from the event.
     */
    public void removeOrganizerFromEvent(UUID eventId, String organizerUsername) {
        log.info("Removing organizer '{}' from event '{}'", organizerUsername, eventId);

        Event event = eventRepository.findEventById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        if (event.getOrganizer() == null) {
            log.warn("Event '{}' does not have an organizer", eventId);
            throw new IllegalStateException("Event does not have an organizer");
        }

        if (!event.getOrganizer().getUsername().equals(organizerUsername)) {
            log.warn("Organizer '{}' is not assigned to event '{}'", organizerUsername, eventId);
            throw new IllegalStateException("Organizer is not assigned to this event");
        }

        event.setOrganizer(null);
        eventRepository.save(event);

        log.info("Successfully removed organizer '{}' from event '{}'", organizerUsername, eventId);
    }

    /**
     * Assigns a location to an event.
     *
     * @param eventId                         The ID of the event to assign the location to.
     * @param eventParticipantUsernameDTOList The DTO containing participants usernames to assign to the event.
     */
    @Transactional
    public void assignParticipantToEvent(UUID eventId, List<EventParticipantUsernameDTO> eventParticipantUsernameDTOList) {
        log.info("Assigning participants to event with ID: {}", eventId);

        // Validate input
        if (eventParticipantUsernameDTOList == null || eventParticipantUsernameDTOList.isEmpty()) {
            throw new IllegalArgumentException("Participant list cannot be null or empty");
        }

        // Find the event
        Event event = eventRepository.findEventById(eventId)
                .orElseThrow(() -> {
                    log.error(LOG_MESSAGE_EVENT_NOT_FOUND, eventId);
                    return new EventNotFoundException(eventId);
                });

        // Create a list to hold the participants
        List<User> participants = new ArrayList<>();

        // Iterate over the list of DTOs to find and add participants
        for (EventParticipantUsernameDTO eventParticipantUsernameDTO : eventParticipantUsernameDTOList) {
            String username = eventParticipantUsernameDTO.getUsername();
            User participant = userRepository.findByUsername(username)
                    .orElseThrow(() -> {
                        log.error("User not found with username: {}", username);
                        return new UsernameNotFoundException(username);
                    });
            participants.add(participant);
            log.info("Added participant with username: {}", username);
        }

        // Add participants to the event (avoid duplicates)
        Set<User> uniqueParticipants = new HashSet<>(event.getParticipants());
        uniqueParticipants.addAll(participants);
        event.setParticipants(new ArrayList<>(uniqueParticipants));

        // Save the updated event
        eventRepository.save(event);
        log.info("Participants assigned successfully to event with ID: {}", eventId);
    }

    /**
     * Removes a participant from an event.
     *
     * @param eventId        The ID of the event to remove the participant from.
     * @param participantDTO The DTO containing the participant username to remove from the event.
     */
    @Transactional
    public void removeParticipantFromEvent(UUID eventId, EventParticipantUsernameDTO participantDTO) {
        log.info("Attempting to remove participant '{}' from event '{}'", participantDTO.getUsername(), eventId);

        Event event = eventRepository.findEventById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        User participant = userRepository.findById(participantDTO.getUsername())
                .orElseThrow(() -> new RecordNotFoundException("Participant not found with username: " + participantDTO.getUsername()));

        if (!event.getParticipants().remove(participant)) {
            log.warn("Participant '{}' was not found in event '{}'", participantDTO.getUsername(), eventId);
            throw new IllegalStateException("Participant is not registered for this event.");
        }

        eventRepository.save(event);
        log.info("Successfully removed participant '{}' from event '{}'", participantDTO.getUsername(), eventId);
    }

    /**
     * Assigns a location to an event.
     *
     * @param eventId         The ID of the event to assign the location to.
     * @param ticketIdDTOList The DTO containing the ticket IDs to assign to the event.
     */
    @Transactional
    public void addTicketsToEvent(UUID eventId, List<EventTicketIdDTO> ticketIdDTOList) {
        log.info("Adding ticket to event with ID: {}", eventId);

        if (ticketIdDTOList == null || ticketIdDTOList.isEmpty()) {
            throw new IllegalArgumentException("Ticket list cannot be null or empty");
        }

        // Find the event
        Event event = eventRepository.findEventById(eventId)
                .orElseThrow(() -> {
                    log.error(LOG_MESSAGE_EVENT_NOT_FOUND, eventId);
                    return new EventNotFoundException(eventId);
                });

        // Create a list to hold the tickets
        List<Ticket> tickets = new ArrayList<>();

        // Iterate over the list of DTOs to find and add tickets
        for (EventTicketIdDTO ticketIdDTO : ticketIdDTOList) {
            UUID ticketId = ticketIdDTO.getTicketId();
            Ticket ticket = ticketRepository.findById(ticketId)
                    .orElseThrow(() -> {
                        log.error("Ticket not found with ID: {}", ticketId);
                        return new RecordNotFoundException("Ticket not found with ID: " + ticketId);
                    });
            tickets.add(ticket);
            log.info("Added ticket with ID: {}", ticketId);
        }

        // Add tickets to the event (avoid duplicates)
        Set<Ticket> uniqueTickets = new HashSet<>(event.getTickets());
        uniqueTickets.addAll(tickets);
        event.setTickets(new ArrayList<>(uniqueTickets));

        // Save the updated event
        eventRepository.save(event);
        log.info("Ticket added successfully to event with ID: {}", eventId);
    }

    /**
     * Removes a ticket from an event.
     *
     * @param eventId     The ID of the event to remove the ticket from.
     * @param ticketIdDTO The DTO containing the ticket ID to remove from the event.
     */
    @Transactional
    public void removeTicketFromEvent(UUID eventId, EventTicketIdDTO ticketIdDTO) {
        log.info("Attempting to remove ticket '{}' from event '{}'", ticketIdDTO.getTicketId(), eventId);

        Event event = eventRepository.findEventById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        Ticket ticket = ticketRepository.findById(ticketIdDTO.getTicketId())
                .orElseThrow(() -> new RecordNotFoundException("Ticket not found with ID: " + ticketIdDTO.getTicketId()));

        if (!event.getTickets().remove(ticket)) {
            log.warn("Ticket '{}' was not found in event '{}'", ticketIdDTO.getTicketId(), eventId);
            throw new IllegalStateException("Ticket is not registered for this event.");
        }

        eventRepository.save(event);
        log.info("Successfully removed ticket '{}' from event '{}'", ticketIdDTO.getTicketId(), eventId);
    }

    /**
     * Assigns a location to an event.
     *
     * @param eventId           The ID of the event to assign the location to.
     * @param feedbackIdDTOList The DTO containing the feedback IDs to assign to the event.
     */
    @Transactional
    public void AddFeedbacksToEvent(UUID eventId, List<EventFeedbackIdDTO> feedbackIdDTOList) {
        log.info("Adding feedback to event with ID: {}", eventId);

        if (feedbackIdDTOList == null || feedbackIdDTOList.isEmpty()) {
            throw new IllegalArgumentException("Feedback list cannot be null or empty");
        }

        // Find the event
        Event event = eventRepository.findEventById(eventId)
                .orElseThrow(() -> {
                    log.error(LOG_MESSAGE_EVENT_NOT_FOUND, eventId);
                    return new EventNotFoundException(eventId);
                });

        // Create a list to hold the feedbacks
        List<Feedback> feedbacks = new ArrayList<>();

        // Iterate over the list of DTOs to find and add feedbacks
        for (EventFeedbackIdDTO feedbackIdDTO : feedbackIdDTOList) {
            UUID feedbackId = feedbackIdDTO.getFeedbackId();
            Feedback feedback = feedbackRepository.findById(feedbackId)
                    .orElseThrow(() -> {
                        log.error("Feedback not found with ID: {}", feedbackId);
                        return new RecordNotFoundException("Feedback not found with ID: " + feedbackId);
                    });
            feedbacks.add(feedback);
            log.info("Added feedback with ID: {}", feedbackId);
        }

        // Add feedbacks to the event (avoid duplicates)
        Set<Feedback> uniqueFeedbacks = new HashSet<>(event.getFeedbacks());
        uniqueFeedbacks.addAll(feedbacks);

        event.setFeedbacks(new ArrayList<>(uniqueFeedbacks));

        // Save the updated event
        eventRepository.save(event);
        log.info("Feedback added successfully to event with ID: {}", eventId);
    }

    /**
     * Removes a feedback from an event.
     *
     * @param eventId       The ID of the event to remove the feedback from.
     * @param feedbackIdDTO The DTO containing the feedback ID to remove from the event.
     */
    @Transactional
    public void removeFeedbackFromEvent(UUID eventId, EventFeedbackIdDTO feedbackIdDTO) {
        log.info("Attempting to remove feedback '{}' from event '{}'", feedbackIdDTO.getFeedbackId(), eventId);

        Event event = eventRepository.findEventById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        Feedback feedback = feedbackRepository.findById(feedbackIdDTO.getFeedbackId())
                .orElseThrow(() -> new RecordNotFoundException("Feedback not found with ID: " + feedbackIdDTO.getFeedbackId()));

        if (!event.getFeedbacks().remove(feedback)) {
            log.warn("Feedback '{}' was not found in event '{}'", feedbackIdDTO.getFeedbackId(), eventId);
            throw new IllegalStateException("Feedback is not registered for this event.");
        }

        eventRepository.save(event);
        log.info("Successfully removed feedback '{}' from event '{}'", feedbackIdDTO.getFeedbackId(), eventId);
    }
}
