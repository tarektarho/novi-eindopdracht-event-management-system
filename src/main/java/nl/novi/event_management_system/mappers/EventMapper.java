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

        return EventResponseDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .location(event.getLocation())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .capacity(event.getCapacity())
                .price(event.getPrice())
                .organizerUsername(Optional.ofNullable(event.getOrganizer()).map(User::getUsername).orElse(null))
                .organizer(Optional.ofNullable(event.getOrganizer()).map(UserMapper::toUserProfileResponseDTO).orElse(null))
                .ticketList(mapTicketsToDTO(event.getTickets()))
                .feedbackList(mapFeedbacksToDTO(event.getFeedbacks()))
                .participants(mapParticipantsToDTO(event.getParticipants()))
                .build();
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
        return Event.builder()
                .name(eventCreateDTO.getName())
                .location(eventCreateDTO.getLocation())
                .startDate(eventCreateDTO.getStartDate())
                .endDate(eventCreateDTO.getEndDate())
                .capacity(eventCreateDTO.getCapacity())
                .price(eventCreateDTO.getPrice())
                .build();
    }

    /**
     * Maps a list of Ticket entities to a list of EventTicketIdDTOs.
     *
     * @param tickets The list of Ticket entities to map.
     * @return A list of EventTicketIdDTOs.
     */
    private static List<EventTicketIdDTO> mapTicketsToDTO(List<Ticket> tickets) {
        return Optional.ofNullable(tickets)
                .orElseGet(List::of)
                .stream()
                .map(ticket -> EventIdInputMapper.toTicketIdDTO(ticket.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Maps a list of Feedback entities to a list of EventIdInputDTOs.
     *
     * @param feedbacks The list of Feedback entities to map.
     * @return A list of EventIdInputDTOs.
     */
    private static List<EventFeedbackIdDTO> mapFeedbacksToDTO(List<Feedback> feedbacks) {
        return Optional.ofNullable(feedbacks)
                .orElseGet(List::of)
                .stream()
                .map(feedback -> EventIdInputMapper.toFeedbackIdDTO(feedback.getId()))
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