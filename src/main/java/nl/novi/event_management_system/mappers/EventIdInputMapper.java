package nl.novi.event_management_system.mappers;

import nl.novi.event_management_system.dtos.eventDtos.EventFeedbackIdDTO;
import nl.novi.event_management_system.dtos.eventDtos.EventTicketIdDTO;

import java.util.UUID;

public class EventIdInputMapper {

    /**
     * Maps a UUID to an EventTicketIdDTO
     *
     * @param ticketId UUID
     * @return EventTicketIdDTO
     */
    public static EventTicketIdDTO toTicketIdDTO(UUID ticketId) {
        EventTicketIdDTO eventTicketIdDTO = new EventTicketIdDTO();
        eventTicketIdDTO.setTicketId(ticketId);
        return eventTicketIdDTO;
    }

    /**
     * Maps a UUID to an EventFeedbackIdDTO
     *
     * @param feedbackId UUID
     * @return EventFeedbackIdDTO
     */
    public static EventFeedbackIdDTO toFeedbackIdDTO(UUID feedbackId) {
        EventFeedbackIdDTO eventFeedbackIdDTO = new EventFeedbackIdDTO();
        eventFeedbackIdDTO.setFeedbackId(feedbackId);
        return eventFeedbackIdDTO;
    }
}
