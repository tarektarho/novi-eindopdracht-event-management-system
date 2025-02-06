package nl.novi.event_management_system.dtos.feedbackDtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
public class FeedbackCreateDTO {
    private String comment;
    private int rating;
    @Schema(hidden = true)
    private String username;
    @Schema(hidden = true)
    private UUID eventId;
}
