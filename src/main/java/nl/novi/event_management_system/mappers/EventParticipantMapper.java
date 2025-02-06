package nl.novi.event_management_system.mappers;

import nl.novi.event_management_system.dtos.eventDtos.EventParticipantUsernameDTO;

public class EventParticipantMapper {

    /**
     * Converts a username to an EventParticipantUsernameDTO.
     *
     * @param participantUsername The username to convert.
     * @return The corresponding EventParticipantUsernameDTO.
     */
    public static EventParticipantUsernameDTO toParticipantDto(String participantUsername) {
        EventParticipantUsernameDTO dto = new EventParticipantUsernameDTO();
        dto.setUsername(participantUsername);
        return dto;
    }

}
