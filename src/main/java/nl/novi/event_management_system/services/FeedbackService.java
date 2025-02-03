package nl.novi.event_management_system.services;

import lombok.extern.slf4j.Slf4j;
import nl.novi.event_management_system.dtos.FeedbackDTO;
import nl.novi.event_management_system.exceptions.EventNotFoundException;
import nl.novi.event_management_system.exceptions.RecordNotFoundException;
import nl.novi.event_management_system.exceptions.UsernameNotFoundException;
import nl.novi.event_management_system.mappers.FeedbackMapper;
import nl.novi.event_management_system.models.Feedback;
import nl.novi.event_management_system.models.User;
import nl.novi.event_management_system.models.Event;
import nl.novi.event_management_system.repositories.FeedbackRepository;
import nl.novi.event_management_system.repositories.UserRepository;
import nl.novi.event_management_system.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    public FeedbackDTO submitFeedback(FeedbackDTO feedbackDTO) {
        User user;
        Event event;

        if (feedbackDTO.getUsername() != null) {
            user = userRepository.findByUsername(feedbackDTO.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException(feedbackDTO.getUsername()));
            feedbackDTO.setUsername(user.getUsername());
        }

        if (feedbackDTO.getEventId() != null) {
            event = eventRepository.findById(feedbackDTO.getEventId())
                    .orElseThrow(() -> new EventNotFoundException(feedbackDTO.getEventId()));
            feedbackDTO.setEventId(event.getId());
        }

        Feedback feedback = FeedbackMapper.toEntity(feedbackDTO);
        feedbackRepository.save(feedback);
        return FeedbackMapper.toResponseDTO(feedback);
    }

    public FeedbackDTO getFeedbackById(UUID id) {
        log.info("Fetching feedback from repository with ID: {}", id);

        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Feedback not found with ID: " + id));

        log.info("Feedback retrieved successfully: {}", feedback.getId());
        return FeedbackMapper.toResponseDTO(feedback);
    }

    public List<FeedbackDTO> getAllFeedbacks() {
        log.info("Fetching all feedbacks from repository");
        return FeedbackMapper.toResponseDTOList(feedbackRepository.findAll());
    }

    public FeedbackDTO updateFeedback(UUID id, FeedbackDTO feedbackDTO) {
        log.info("Updating feedback with ID: {}", id);

        // Fetch existing feedback or throw an exception if not found
        Feedback existingFeedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Feedback not found with ID: " + id));

        // Map updated values to the existing entity instead of creating a new one
        existingFeedback.setComment(feedbackDTO.getComment());
        existingFeedback.setRating(feedbackDTO.getRating());

        // If a username is provided, update the user association
        if (feedbackDTO.getUsername() != null) {
            User user = userRepository.findByUsername(feedbackDTO.getUsername())
                    .orElseThrow(() -> new RecordNotFoundException("User not found with username: " + feedbackDTO.getUsername()));
            existingFeedback.setUser(user);
        }

        // Save the updated feedback
        Feedback savedFeedback = feedbackRepository.save(existingFeedback);
        log.info("Feedback updated successfully: {}", savedFeedback.getId());

        return FeedbackMapper.toResponseDTO(savedFeedback);
    }

    public void deleteFeedback(UUID id) {
        log.info("Attempting to delete feedback with ID: {}", id);

        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Feedback not found with ID: " + id));

        feedbackRepository.delete(feedback);
        log.info("Feedback deleted successfully with ID: {}", id);
    }

    public List<Feedback> getFeedbackForEvent(UUID eventId) {
        return feedbackRepository.findByEventId(eventId);
    }

    public List<Feedback> getFeedbackByUser(String username) {
        return feedbackRepository.findByUserUsername(username);
    }
}
