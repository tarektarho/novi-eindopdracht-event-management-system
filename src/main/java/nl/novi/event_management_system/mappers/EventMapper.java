package nl.novi.event_management_system.mappers;

import jakarta.validation.Valid;
import nl.novi.event_management_system.dtos.eventDtos.EventCreateDTO;
import nl.novi.event_management_system.dtos.eventDtos.EventResponseDTO;
import nl.novi.event_management_system.models.Event;

import java.util.List;
import java.util.stream.Collectors;

public class EventMapper {


    public static EventResponseDTO toResponseDTO(Event event) {
        if (event == null){
            return null;
        }

        EventResponseDTO dto = new EventResponseDTO();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setLocation(event.getLocation());
        dto.setStartTime(event.getStartTime());
        dto.setEndTime(event.getEndTime());
        dto.setCapacity(event.getCapacity());
        dto.setPrice(event.getPrice());

        if(event.getOrganizer() != null) {
            dto.setOrganizer(UserMapper.toUserProfileResponseDTO(event.getOrganizer()));
        }

        if(event.getTickets() != null) {
            dto.setTicketList(TicketMapper.toResponseDTOList(event.getTickets()));
        }

        if(event.getFeedbacks() != null) {
            dto.setFeedbackList(FeedbackMapper.toResponseDTOList(event.getFeedbacks()));
        }

        return dto;
    }

    public static List<EventResponseDTO> toResponseDTOList(List<Event> events) {
        return events.stream()
                .map(EventMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public static Event toEntity(@Valid EventCreateDTO eventCreateDTO) {
        Event event = new Event();
        event.setName(eventCreateDTO.getName());
        event.setLocation(eventCreateDTO.getLocation());
        event.setStartTime(eventCreateDTO.getStartTime());
        event.setEndTime(eventCreateDTO.getEndTime());
        event.setCapacity(eventCreateDTO.getCapacity());
        event.setPrice(eventCreateDTO.getPrice());

        return event;
    }

}
