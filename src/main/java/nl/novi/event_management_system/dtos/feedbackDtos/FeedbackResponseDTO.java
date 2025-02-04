package nl.novi.event_management_system.dtos.feedbackDtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeedbackResponseDTO {
    private UUID id;
    private String comment;
    private int rating;
    private String username;
    private LocalDate feedbackDate;
    private UUID eventId;
}
