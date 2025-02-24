package nl.novi.event_management_system.mappers;

import jakarta.validation.Valid;
import nl.novi.event_management_system.dtos.feedbackDtos.FeedbackCreateDTO;
import nl.novi.event_management_system.dtos.feedbackDtos.FeedbackResponseDTO;
import nl.novi.event_management_system.models.Feedback;

import java.util.List;
import java.util.stream.Collectors;

public class FeedbackMapper {

    /**
     * Maps a Feedback object to a FeedbackResponseDTO object
     *
     * @param feedback Feedback
     * @return FeedbackResponseDTO
     */
    public static FeedbackResponseDTO toResponseDTO(Feedback feedback) {
        if (feedback == null) {
            return null;
        }

        return FeedbackResponseDTO.builder()
                .id(feedback.getId())
                .comment(feedback.getComment())
                .rating(feedback.getRating())
                .username(feedback.getUser() != null ? feedback.getUser().getUsername() : null)
                .feedbackDate(feedback.getFeedbackDate())
                .eventId(feedback.getEvent() != null ? feedback.getEvent().getId() : null)
                .build();
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
