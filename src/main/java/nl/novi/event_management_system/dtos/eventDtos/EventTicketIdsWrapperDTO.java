package nl.novi.event_management_system.dtos.eventDtos;

import java.util.List;

public class EventTicketIdsWrapperDTO {
    private List<EventTicketIdDTO> ticketIds;

    public List<EventTicketIdDTO> getTicketIds() {
        return ticketIds;
    }

    public void setTicketIds(List<EventTicketIdDTO> ticketIds) {
        this.ticketIds = ticketIds;
    }
}
