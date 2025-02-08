package nl.novi.event_management_system.dtos.eventDtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventOrganizerUsernameDTO {
    @NotNull(message = "Organizer Username is mandatory")
    private String username;
}
