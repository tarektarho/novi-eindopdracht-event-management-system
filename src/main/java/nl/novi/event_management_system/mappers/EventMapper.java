package nl.novi.event_management_system.mappers;

import jakarta.validation.Valid;
import nl.novi.event_management_system.dtos.eventDtos.*;
import nl.novi.event_management_system.models.Event;
import nl.novi.event_management_system.models.Feedback;
import nl.novi.event_management_system.models.Ticket;
import nl.novi.event_management_system.models.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EventMapper {

    /**
     * Converts an Event entity to an EventResponseDTO.
     *
     * @param event The Event entity to convert.
     * @return The corresponding EventResponseDTO, or null if the input is null.
     */
    public static EventResponseDTO toResponseDTO(Event event) {
        if (event == null) {
            return null;
        }

        EventResponseDTO dto = new EventResponseDTO();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setLocation(event.getLocation());
        dto.setStartDate(event.getStartDate());
        dto.setEndDate(event.getEndDate());
        dto.setCapacity(event.getCapacity());
        dto.setPrice(event.getPrice());

        // Map organizer
        Optional.ofNullable(event.getOrganizer())
                .ifPresent(organizer -> dto.setOrganizer(UserMapper.toUserProfileResponseDTO(organizer)));

        // Map tickets
        dto.setTicketList(mapTicketsToDTO(event.getTickets()));

        // Map feedbacks
        dto.setFeedbackList(mapFeedbacksToDTO(event.getFeedbacks()));

        // Map participants
        dto.setParticipants(mapParticipantsToDTO(event.getParticipants()));

        return dto;
    }

    /**
     * Converts a list of Event entities to a list of EventResponseDTOs.
     *
     * @param events The list of Event entities to convert.
     * @return A list of EventResponseDTOs.
     */
    public static List<EventResponseDTO> toResponseDTOList(List<Event> events) {
        return Optional.ofNullable(events)
                .orElseGet(List::of)
                .stream()
                .map(EventMapper::toResponseDTO)
                .collect(Collectors.toList());
    }


    /**
     * Converts an EventCreateDTO to an Event entity.
     *
     * @param eventCreateDTO The EventCreateDTO to convert.
     * @return The corresponding Event entity.
     */
    public static Event toEntity(@Valid EventCreateDTO eventCreateDTO) {
        Event event = new Event();
        event.setName(eventCreateDTO.getName());
        event.setLocation(eventCreateDTO.getLocation());
        event.setStartDate(eventCreateDTO.getStartDate());
        event.setEndDate(eventCreateDTO.getEndDate());
        event.setCapacity(eventCreateDTO.getCapacity());
        event.setPrice(eventCreateDTO.getPrice());

        return event;
    }

    /**
     * Maps a list of Ticket entities to a list of EventTicketIdDTOs.
     *
     * @param tickets The list of Ticket entities to map.
     * @return A list of EventTicketIdDTOs.
     */
    private static List<EventIdInputDTO> mapTicketsToDTO(List<Ticket> tickets) {
        return Optional.ofNullable(tickets)
                .orElseGet(List::of)
                .stream()
                .map(ticket -> EventIdInputMapper.toIdDTO(ticket.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Maps a list of Feedback entities to a list of EventIdInputDTOs.
     *
     * @param feedbacks The list of Feedback entities to map.
     * @return A list of EventIdInputDTOs.
     */
    private static List<EventIdInputDTO> mapFeedbacksToDTO(List<Feedback> feedbacks) {
        return Optional.ofNullable(feedbacks)
                .orElseGet(List::of)
                .stream()
                .map(feedback -> EventIdInputMapper.toIdDTO(feedback.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Maps a list of User entities to a list of EventUsernameInputDTOs.
     *
     * @param participants The list of User entities to map.
     * @return A list of EventUsernameInputDTOs.
     */
    private static List<EventParticipantUsernameDTO> mapParticipantsToDTO(List<User> participants) {
        return Optional.ofNullable(participants)
                .orElseGet(List::of)
                .stream()
                .map(user -> EventParticipantMapper.toParticipantDto(user.getUsername()))
                .collect(Collectors.toList());
    }
}