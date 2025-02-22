package nl.novi.event_management_system.dtos.eventDtos;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class EventFeedbackIdWrapperDTO {
    @NotNull(message = "Feedback ID is mandatory")
    private List<EventFeedbackIdDTO> feedbackIds;

    public List<EventFeedbackIdDTO> getFeedbackIds() {
        return feedbackIds;
    }

    public void setFeedbackIds(List<EventFeedbackIdDTO> feedbackIds) {
        this.feedbackIds = feedbackIds;
    }
}
