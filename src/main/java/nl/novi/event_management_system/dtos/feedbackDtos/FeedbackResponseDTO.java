package nl.novi.event_management_system.dtos.feedbackDtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FeedbackResponseDTO {
    private UUID id;
    private String comment;
    private int rating;
    private String username;
    private LocalDate feedbackDate;
    private UUID eventId;
}
