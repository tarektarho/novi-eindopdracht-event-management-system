package nl.novi.event_management_system.dtos.eventDtos;

import java.util.List;

public class EventFeedbackIdWrapperDTO {
    private List<EventFeedbackIdDTO> feedbackIds;

    public List<EventFeedbackIdDTO> getFeedbackIds() {
        return feedbackIds;
    }

    public void setFeedbackIds(List<EventFeedbackIdDTO> feedbackIds) {
        this.feedbackIds = feedbackIds;
    }
}
