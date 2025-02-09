package nl.novi.event_management_system.dtos.eventDtos;

import jakarta.validation.constraints.NotNull;

public class EventOrganizerUsernameDTO {
    @NotNull(message = "Organizer Username is mandatory")
    private String username;

    public EventOrganizerUsernameDTO() {
    }

    public EventOrganizerUsernameDTO(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
