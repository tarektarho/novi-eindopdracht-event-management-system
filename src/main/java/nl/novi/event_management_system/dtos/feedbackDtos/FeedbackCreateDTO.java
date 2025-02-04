package nl.novi.event_management_system.dtos.feedbackDtos;

import lombok.Data;

import java.util.UUID;

@Data
public class FeedbackCreateDTO {
    private String comment;
    private int rating;
    private String username;
    private UUID eventId;
}
