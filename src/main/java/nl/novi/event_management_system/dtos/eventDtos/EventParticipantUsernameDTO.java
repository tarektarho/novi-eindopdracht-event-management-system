package nl.novi.event_management_system.dtos.eventDtos;

import jakarta.validation.constraints.NotNull;

public class EventParticipantUsernameDTO {
    @NotNull(message = "Participant Username is mandatory")
    private String username;

    public EventParticipantUsernameDTO() {
    }

    public EventParticipantUsernameDTO(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
