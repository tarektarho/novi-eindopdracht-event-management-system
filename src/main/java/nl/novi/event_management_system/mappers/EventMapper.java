package nl.novi.event_management_system.mappers;

import jakarta.validation.Valid;
import nl.novi.event_management_system.dtos.eventDtos.EventCreateDTO;
import nl.novi.event_management_system.dtos.eventDtos.EventResponseDTO;
import nl.novi.event_management_system.models.Event;
import nl.novi.event_management_system.models.User;
import nl.novi.event_management_system.repositories.UserPhotoRepository;

import java.util.List;
import java.util.stream.Collectors;

public class EventMapper {


    public static EventResponseDTO toResponseDTO(Event event) {
        if (event == null){
            return null;
        }

        EventResponseDTO dto = new EventResponseDTO();
        dto.setId(event.getId());
        dto.setOrganizerUsername(event.getOrganizer().getUsername()); // Using username instead of full User object
        dto.setName(event.getName());
        dto.setLocation(event.getLocation());
        dto.setStartTime(event.getStartTime());
        dto.setEndTime(event.getEndTime());
        dto.setCapacity(event.getCapacity());
        dto.setPrice(event.getPrice());


        if(event.getOrganizer() != null) {
            dto.setOrganizer(UserMapper.toUserResponseDTO(event.getOrganizer()));
        }

        //Todo implement this when ticket service is ready
//        if(event.getTickets() != null) {
//        }

        //Todo implement this when feedbackSerivce is implemented.
//        if(event.getFeedbacks() != null){
//
//        }

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
