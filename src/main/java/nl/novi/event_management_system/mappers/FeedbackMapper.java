package nl.novi.event_management_system.mappers;

import nl.novi.event_management_system.dtos.FeedbackDTO;
import nl.novi.event_management_system.models.Feedback;

public class FeedbackMapper {

    // Convert Feedback entity to FeedbackDTO
    public static FeedbackDTO toDTO(Feedback feedback) {
        FeedbackDTO feedbackDTO = new FeedbackDTO();
        feedbackDTO.setId(feedback.getId());
        feedbackDTO.setComment(feedback.getComment());
        feedbackDTO.setRating(feedback.getRating());

        // Map User and Event details
        if (feedback.getUser() != null) {
            feedbackDTO.setUsername(feedback.getUser().getUsername());
        }
        if (feedback.getEvent() != null) {
            feedbackDTO.setEventId(feedback.getEvent().getId());
        }

        return feedbackDTO;
    }

    // Convert FeedbackDTO to Feedback entity
    public static Feedback toEntity(FeedbackDTO feedbackDTO) {
        Feedback feedback = new Feedback();
        feedback.setId(feedbackDTO.getId());
        feedback.setComment(feedbackDTO.getComment());
        feedback.setRating(feedbackDTO.getRating());

        return feedback;
    }
}
