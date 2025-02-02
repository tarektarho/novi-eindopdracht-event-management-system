package nl.novi.event_management_system.dtos.eventDtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import nl.novi.event_management_system.dtos.FeedbackDTO;
import nl.novi.event_management_system.dtos.ticketDtos.TicketResponseDTO;
import nl.novi.event_management_system.dtos.userDtos.UserProfileDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)  // This will exclude null fields from the response
public class EventResponseDTO {
    private UUID id;
    private String organizerUsername;
    private String name;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int capacity;
    private double price;
    private UserProfileDTO organizer;
    private List<TicketResponseDTO> ticketList;
    private List<FeedbackDTO> feedbackList;

}
