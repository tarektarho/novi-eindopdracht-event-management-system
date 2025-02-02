package nl.novi.event_management_system.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeedbackDTO {

    private UUID id;
    private String comment;
    private int rating;
    private LocalDateTime feedbackDate;
    private String username; // User that provided the feedback
    private UUID eventId;    // ID of the event the feedback is associated with
}
