package nl.novi.event_management_system.dtos.userDtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import nl.novi.event_management_system.dtos.FeedbackDTO;
import nl.novi.event_management_system.dtos.ticketDtos.TicketResponseDTO;
import nl.novi.event_management_system.models.Role;
import nl.novi.event_management_system.models.UserPhoto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)  // This will exclude null fields from the response
public class UserResponseDTO {
    private String username;
    private String email;
    private String password;
    private Boolean enabled;
    private Set<Role> roles = new HashSet<>();
    private UserPhoto userPhoto;
    private List<TicketResponseDTO> tickets = new ArrayList<>();
    private List<FeedbackDTO> feedbackList = new ArrayList<>();
}
