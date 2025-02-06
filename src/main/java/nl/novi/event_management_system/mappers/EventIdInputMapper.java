package nl.novi.event_management_system.mappers;

import nl.novi.event_management_system.dtos.eventDtos.EventIdInputDTO;

import java.util.UUID;

public class EventIdInputMapper {

    public static EventIdInputDTO toIdDTO(UUID id) {
        EventIdInputDTO eventIdInputDTO = new EventIdInputDTO();
        eventIdInputDTO.setId(id);
        return eventIdInputDTO;
    }
}
