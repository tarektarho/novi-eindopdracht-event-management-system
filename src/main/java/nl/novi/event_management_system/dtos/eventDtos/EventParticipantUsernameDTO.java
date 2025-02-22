package nl.novi.event_management_system.dtos.eventDtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventParticipantUsernameDTO {
    @NotNull(message = "Participant Username is mandatory")
    private String username;
}
