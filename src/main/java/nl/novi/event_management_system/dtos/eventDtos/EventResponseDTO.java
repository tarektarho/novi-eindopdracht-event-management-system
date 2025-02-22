package nl.novi.event_management_system.dtos.eventDtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import nl.novi.event_management_system.dtos.userDtos.UserProfileDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class EventResponseDTO {
    private UUID id;
    private String organizerUsername;
    private String name;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private int capacity;
    private double price;
    private UserProfileDTO organizer;
    private List<EventTicketIdDTO> ticketList;
    private List<EventFeedbackIdDTO> feedbackList;
    private List<EventParticipantUsernameDTO> participants;
}
