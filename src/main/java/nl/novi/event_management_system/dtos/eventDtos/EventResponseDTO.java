package nl.novi.event_management_system.dtos.eventDtos;

import lombok.Getter;
import lombok.Setter;
import nl.novi.event_management_system.dtos.FeedbackDTO;
import nl.novi.event_management_system.dtos.ticketDtos.TicketResponseDTO;
import nl.novi.event_management_system.dtos.userDtos.UserResponseDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class EventResponseDTO {
    private UUID id;
    private String organizerUsername;
    private String name;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int capacity;
    private double price;
    private UserResponseDTO organizer;
    private List<TicketResponseDTO> ticketList;
    private List<FeedbackDTO> feedbackList;

}
