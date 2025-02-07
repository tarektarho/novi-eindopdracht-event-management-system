package nl.novi.event_management_system.services;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class EventService {
    String LOG_MESSAGE_EVENT_NOT_FOUND = "Event not found with ID: {}";

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;
    private final TicketRepository ticketRepository;

    public EventService(EventRepository eventRepository, UserRepository userRepository, FeedbackRepository feedbackRepository, TicketRepository ticketRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.feedbackRepository = feedbackRepository;
        this.ticketRepository = ticketRepository;
    }

    public EventResponseDTO createEvent(@Valid EventCreateDTO eventCreateDTO) {
        Event event = EventMapper.toEntity(eventCreateDTO);
        eventRepository.save(event);
        return EventMapper.toResponseDTO(event);
    }

    public EventResponseDTO findEventById(UUID id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
        return EventMapper.toResponseDTO(event);
    }

    public List<EventResponseDTO> getEventsByOrganizer(String username) {

        return EventMapper.toResponseDTOList(eventRepository.findByOrganizerUsername(username));
    }

    public List<EventResponseDTO> getAllEvents() {
        return EventMapper.toResponseDTOList(eventRepository.findAll());
    }

    public EventResponseDTO updateEvent(UUID id, EventCreateDTO eventCreateDTO) {
        if (!eventRepository.existsById(id)) throw new EventNotFoundException(id);
        Event storedEvent = eventRepository.findEventById(id).orElseThrow(() -> new EventNotFoundException(id));
        Event updatedEvent = EventMapper.toEntity(eventCreateDTO);
        updatedEvent.setId(storedEvent.getId());
        User user = new User();
        if (eventCreateDTO.getOrganizerUsername() != null) {
            user = userRepository.findByUsername(eventCreateDTO.getOrganizerUsername())
                    .orElseThrow(() -> new UsernameNotFoundException(eventCreateDTO.getOrganizerUsername()));
        }
        updatedEvent.setOrganizer(user);
        Event savedEvent = eventRepository.save(updatedEvent);

        return EventMapper.toResponseDTO(savedEvent);
    }

    public boolean deleteEventById(UUID id) {
        eventRepository.findEventById(id)
                .orElseThrow(() -> new EventNotFoundException(id));

        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return true;
        }
        return false;
    }

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

        List<Ticket> tickets = new ArrayList<>();

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

        List<Feedback> feedbacks = new ArrayList<>();

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
