package nl.novi.event_management_system.services;

import lombok.extern.slf4j.Slf4j;
import nl.novi.event_management_system.dtos.feedbackDtos.FeedbackCreateDTO;
import nl.novi.event_management_system.dtos.feedbackDtos.FeedbackResponseDTO;
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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public FeedbackService(FeedbackRepository feedbackRepository, UserRepository userRepository, EventRepository eventRepository) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    public FeedbackResponseDTO submitFeedback(FeedbackCreateDTO feedbackCreateDTO) {
        Feedback feedback = FeedbackMapper.toEntity(feedbackCreateDTO);
        feedbackRepository.save(feedback);
        return FeedbackMapper.toResponseDTO(feedback);
    }

    public FeedbackResponseDTO getFeedbackById(UUID id) {
        log.info("Fetching feedback from repository with ID: {}", id);

        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Feedback not found with ID: " + id));

        log.info("Feedback retrieved successfully: {}", feedback.getId());
        return FeedbackMapper.toResponseDTO(feedback);
    }

    public List<FeedbackResponseDTO> getAllFeedbacks() {
        log.info("Fetching all feedbacks from repository");
        return FeedbackMapper.toResponseDTOList(feedbackRepository.findAll());
    }

    public FeedbackResponseDTO updateFeedback(UUID id, FeedbackCreateDTO feedbackCreateDTO) {
        log.info("Updating feedback with ID: {}", id);

        // Fetch existing feedback or throw an exception if not found
        Feedback existingFeedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Feedback not found with ID: " + id));

        // Map updated values to the existing entity instead of creating a new one
        existingFeedback.setComment(feedbackCreateDTO.getComment());
        existingFeedback.setRating(feedbackCreateDTO.getRating());
        existingFeedback.setId(id);


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

    public List<FeedbackResponseDTO> getEventFeedback(UUID eventId) {
        return FeedbackMapper.toResponseDTOList(feedbackRepository.findByEventId(eventId));
    }

    public List<FeedbackResponseDTO> getUserFeedback(String username) {
        return FeedbackMapper.toResponseDTOList(feedbackRepository.findByUserUsername(username));
    }

    public void assignEventToFeedback(UUID feedbackId, UUID eventId) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RecordNotFoundException("Feedback not found with ID: " + feedbackId));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        feedback.setEvent(event);
        feedbackRepository.save(feedback);
    }

    public void assignUserToFeedback(UUID feedbackId, String username) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RecordNotFoundException("Feedback not found with ID: " + feedbackId));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        feedback.setUser(user);
        feedbackRepository.save(feedback);
    }
}
