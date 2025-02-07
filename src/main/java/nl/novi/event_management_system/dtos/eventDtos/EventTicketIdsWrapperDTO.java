package nl.novi.event_management_system.dtos.eventDtos;

import lombok.Data;

import java.util.List;

@Data
public class EventTicketIdsWrapperDTO {
    private List<EventTicketIdDTO> ticketIds;
}
