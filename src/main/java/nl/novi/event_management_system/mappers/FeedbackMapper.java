package nl.novi.event_management_system.mappers;

import jakarta.validation.Valid;
import nl.novi.event_management_system.dtos.feedbackDtos.FeedbackCreateDTO;
import nl.novi.event_management_system.dtos.feedbackDtos.FeedbackResponseDTO;
import nl.novi.event_management_system.models.Feedback;

import java.util.List;
import java.util.stream.Collectors;

public class FeedbackMapper {

    public static FeedbackResponseDTO toResponseDTO(Feedback feedback) {
        if(feedback == null) {
            return null;
        }

        FeedbackResponseDTO feedbackResponseDTO = new FeedbackResponseDTO();
        feedbackResponseDTO.setFeedbackDate(feedback.getFeedbackDate());
        feedbackResponseDTO.setId(feedback.getId());
        feedbackResponseDTO.setComment(feedback.getComment());
        feedbackResponseDTO.setRating(feedback.getRating());

        if (feedback.getUser() != null) {
            feedbackResponseDTO.setUsername(feedback.getUser().getUsername());
        }

        if (feedback.getEvent() != null) {
            feedbackResponseDTO.setEventId(feedback.getEvent().getId());
        }

        return feedbackResponseDTO;
    }

    public static List<FeedbackResponseDTO> toResponseDTOList(List<Feedback> feedbacks) {
        return feedbacks.stream()
                .map(FeedbackMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public static Feedback toEntity(@Valid FeedbackCreateDTO feedbackCreateDTO) {
        Feedback feedback = new Feedback();
        feedback.setComment(feedbackCreateDTO.getComment());
        feedback.setRating(feedbackCreateDTO.getRating());

        return feedback;
    }
}
