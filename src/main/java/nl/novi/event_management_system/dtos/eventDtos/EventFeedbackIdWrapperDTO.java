package nl.novi.event_management_system.dtos.eventDtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class EventFeedbackIdWrapperDTO {
    @NotNull(message = "Feedback ID is mandatory")
    private List<EventFeedbackIdDTO> feedbackIds;
}
