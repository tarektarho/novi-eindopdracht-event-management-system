package nl.novi.event_management_system.dtos.eventDtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventFeedbackIdDTO {
    @NotNull(message = "feedback ID is mandatory")
    private UUID feedbackId;
}
