package nl.novi.event_management_system.dtos.eventDtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class EventFeedbackIdDTO {
    @NotNull(message = "feedback ID is mandatory")
    private UUID feedbackId;

    public EventFeedbackIdDTO() {
    }

    public EventFeedbackIdDTO(UUID feedbackId) {
        this.feedbackId = feedbackId;
    }

    public UUID getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(UUID feedbackId) {
        this.feedbackId = feedbackId;
    }
}
