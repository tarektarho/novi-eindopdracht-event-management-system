package nl.novi.event_management_system.dtos.userDtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import nl.novi.event_management_system.dtos.eventDtos.EventResponseDTO;
import nl.novi.event_management_system.dtos.feedbackDtos.FeedbackResponseDTO;
import nl.novi.event_management_system.dtos.ticketDtos.TicketResponseDTO;
import nl.novi.event_management_system.models.Role;
import nl.novi.event_management_system.models.UserPhoto;

import java.util.*;

@Data
@NoArgsConstructor
public class UserResponseDTO {
    private String username;
    private String email;
    private String password;
    private Boolean enabled;
    private Set<Role> roles = new HashSet<>();
    private UserPhoto userPhoto;
    private List<TicketResponseDTO> tickets = new ArrayList<>();
    private List<FeedbackResponseDTO> feedbackList = new ArrayList<>();
    private List<EventResponseDTO> eventsOrganized = new ArrayList<>();


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserResponseDTO that = (UserResponseDTO) obj;
        return Objects.equals(username, that.username) &&
                Objects.equals(email, that.email) &&
                Objects.equals(userPhoto, that.userPhoto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email, userPhoto);
    }
}
