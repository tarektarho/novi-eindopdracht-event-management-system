package nl.novi.event_management_system.mappers;

import jakarta.validation.Valid;
import nl.novi.event_management_system.dtos.FeedbackDTO;
import nl.novi.event_management_system.models.Feedback;
import nl.novi.event_management_system.models.User;

import java.util.List;
import java.util.stream.Collectors;

public class FeedbackMapper {

    public static FeedbackDTO toResponseDTO(Feedback feedback) {
        if(feedback == null) {
            return null;
        }

        FeedbackDTO feedbackDTO = new FeedbackDTO();
        feedbackDTO.setFeedbackDate(feedback.getFeedbackDate());
        feedbackDTO.setId(feedback.getId());
        feedbackDTO.setComment(feedback.getComment());
        feedbackDTO.setRating(feedback.getRating());

        if (feedback.getUser() != null) {
            feedbackDTO.setUsername(feedback.getUser().getUsername());
        }

        if (feedback.getEvent() != null) {
            feedbackDTO.setEventId(feedback.getEvent().getId());
        }

        return feedbackDTO;
    }

    public static List<FeedbackDTO> toResponseDTOList(List<Feedback> feedbacks) {
        return feedbacks.stream()
                .map(FeedbackMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public static Feedback toEntity(@Valid FeedbackDTO feedbackDTO) {
        Feedback feedback = new Feedback();
        feedback.setComment(feedbackDTO.getComment());
        feedback.setRating(feedbackDTO.getRating());
        feedback.setFeedbackDate(feedbackDTO.getFeedbackDate());

        if (feedbackDTO.getUsername() != null) {
            User user = new User();
            user.setUsername(feedbackDTO.getUsername());
            feedback.setUser(user);
        }

        return feedback;
    }
}
