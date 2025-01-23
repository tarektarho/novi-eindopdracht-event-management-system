package nl.novi.event_management_system.mappers;

import nl.novi.event_management_system.dtos.eventDtos.EventCreateDTO;
import nl.novi.event_management_system.dtos.eventDtos.EventResponseDTO;
import nl.novi.event_management_system.models.Event;

import java.util.List;
import java.util.stream.Collectors;

public class EventMapper {

    public static EventResponseDTO toEventResponseDTO(Event event) {
        EventResponseDTO eventResponseDTO = new EventResponseDTO();
        eventResponseDTO.setId(event.getId());
        eventResponseDTO.setName(event.getName());
        eventResponseDTO.setDescription(event.getDescription());
        eventResponseDTO.setLocation(event.getLocation());
        eventResponseDTO.setStartDate(event.getStartDate().toString());
        eventResponseDTO.setEndDate(event.getEndDate().toString());
        eventResponseDTO.setMaxParticipants(event.getMaxParticipants());
        eventResponseDTO.setStatus(event.getStatus());

        return eventResponseDTO;
    }


    public static List<EventResponseDTO> toEventResponseDTOList(List<Event> events) {
        return events.stream().map(EventMapper::toEventResponseDTO).collect(Collectors.toList());
    }

    public static Event toEntity(EventCreateDTO eventCreateDTO) {
        if(eventCreateDTO == null) {
            return null;
        }
        Event event = new Event();
        event.setName(eventCreateDTO.getName());
        event.setLocation(eventCreateDTO.getLocation());
        event.setStartDate(eventCreateDTO.getStartDate());
        event.setEndDate(eventCreateDTO.getEndDate());
        event.setMaxParticipants(eventCreateDTO.getMaxParticipants());
        event.setStatus(eventCreateDTO.getStatus());
        event.setDescription(eventCreateDTO.getDescription());
        return event;
    }

}
