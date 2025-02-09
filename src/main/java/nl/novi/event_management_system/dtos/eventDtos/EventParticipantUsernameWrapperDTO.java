package nl.novi.event_management_system.dtos.eventDtos;

import java.util.List;

public class EventParticipantUsernameWrapperDTO {
    private List<EventParticipantUsernameDTO> participants;

    public List<EventParticipantUsernameDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(List<EventParticipantUsernameDTO> participants) {
        this.participants = participants;
    }
}