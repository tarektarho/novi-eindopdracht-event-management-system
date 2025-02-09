package nl.novi.event_management_system.services;

import nl.novi.event_management_system.controllers.GlobalExceptionHandler;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * This class contains the business logic for the Feedback entity.
 */
@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Constructor for the FeedbackService class.
     *
     * @param feedbackRepository The repository for the Feedback entity.
     * @param userRepository     The repository for the User entity.
     * @param eventRepository    The repository for the Event entity.
     */
    public FeedbackService(FeedbackRepository feedbackRepository, UserRepository userRepository, EventRepository eventRepository) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    /**
     * Submits a new feedback.
     *
     * @param feedbackCreateDTO The DTO containing the information for the feedback.
     * @return The DTO containing the information for the created feedback.
     */
    public FeedbackResponseDTO submitFeedback(FeedbackCreateDTO feedbackCreateDTO) {
        Feedback feedback = FeedbackMapper.toEntity(feedbackCreateDTO);
        feedbackRepository.save(feedback);
        return FeedbackMapper.toResponseDTO(feedback);
    }

    /**
     * Retrieves a feedback by its ID.
     *
     * @param id The ID of the feedback to retrieve.
     * @return The DTO containing the information for the feedback.
     */
    public FeedbackResponseDTO getFeedbackById(UUID id) {
        log.info("Fetching feedback from repository with ID: {}", id);

        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Feedback not found with ID: " + id));

        log.info("Feedback retrieved successfully: {}", feedback.getId());
        return FeedbackMapper.toResponseDTO(feedback);
    }

    /**
     * Retrieves all feedbacks.
     *
     * @return A list of DTOs containing the information for all feedbacks.
     */
    public List<FeedbackResponseDTO> getAllFeedbacks() {
        log.info("Fetching all feedbacks from repository");
        return FeedbackMapper.toResponseDTOList(feedbackRepository.findAll());
    }

    /**
     * Updates a feedback.
     *
     * @param id                The ID of the feedback to update.
     * @param feedbackCreateDTO The DTO containing the information for the updated feedback.
     * @return The DTO containing the information for the updated feedback.
     */
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

    /**
     * Deletes a feedback by its ID.
     *
     * @param id The ID of the feedback to delete.
     */
    public void deleteFeedback(UUID id) {
        log.info("Attempting to delete feedback with ID: {}", id);

        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Feedback not found with ID: " + id));

        feedbackRepository.delete(feedback);
        log.info("Feedback deleted successfully with ID: {}", id);
    }

    /**
     * Retrieves all feedbacks for a specific event.
     *
     * @param eventId The ID of the event to retrieve feedback for.
     * @return A list of DTOs containing the information for all feedbacks for the event.
     */
    public List<FeedbackResponseDTO> getEventFeedback(UUID eventId) {
        return FeedbackMapper.toResponseDTOList(feedbackRepository.findByEventId(eventId));
    }

    /**
     * Retrieves all feedbacks for a specific user.
     *
     * @param username The username of the user to retrieve feedback for.
     * @return A list of DTOs containing the information for all feedbacks for the user.
     */
    public List<FeedbackResponseDTO> getUserFeedback(String username) {
        return FeedbackMapper.toResponseDTOList(feedbackRepository.findByUserUsername(username));
    }

    /**
     * Assigns an event to a feedback.
     *
     * @param feedbackId The ID of the feedback to assign the event to.
     * @param eventId    The ID of the event to assign to the feedback.
     */
    public void assignEventToFeedback(UUID feedbackId, UUID eventId) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RecordNotFoundException("Feedback not found with ID: " + feedbackId));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        feedback.setEvent(event);
        feedbackRepository.save(feedback);
    }

    /**
     * Assigns a user to a feedback.
     *
     * @param feedbackId The ID of the feedback to assign the user to.
     * @param username   The username of the user to assign to the feedback.
     */
    public void assignUserToFeedback(UUID feedbackId, String username) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RecordNotFoundException("Feedback not found with ID: " + feedbackId));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        feedback.setUser(user);
        feedbackRepository.save(feedback);
    }
}
