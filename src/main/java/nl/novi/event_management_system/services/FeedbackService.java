package nl.novi.event_management_system.services;

import nl.novi.event_management_system.dtos.FeedbackDTO;
import nl.novi.event_management_system.exceptions.RecordNotFoundException;
import nl.novi.event_management_system.mappers.FeedbackMapper;
import nl.novi.event_management_system.models.Feedback;
import nl.novi.event_management_system.models.User;
import nl.novi.event_management_system.models.Event;
import nl.novi.event_management_system.repositories.FeedbackRepository;
import nl.novi.event_management_system.repositories.UserRepository;
import nl.novi.event_management_system.repositories.EventRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackService {

    //Todo check if need to implement a second service to handle the feedback to user and event
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public FeedbackService(FeedbackRepository feedbackRepository, UserRepository userRepository, EventRepository eventRepository) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    public List<FeedbackDTO> getFeedbacks() {
        List<FeedbackDTO> feedbackList;
        try {
            feedbackList = feedbackRepository.findAll().stream().map(feedback -> {
                FeedbackDTO dto = new FeedbackDTO();
                dto.setId(feedback.getId());
                dto.setComment(feedback.getComment());
                dto.setRating(feedback.getRating());
                dto.setUsername(feedback.getUser().getUsername());
                dto.setEventId(feedback.getEvent().getId());
                return dto;
            }).collect(Collectors.toList());;
        } catch (Exception e) {
            throw new RuntimeException("Could not retrieve feedbacks");
        }
        return feedbackList;
    }

    public FeedbackDTO createFeedback(FeedbackDTO feedbackDTO) {
        Feedback feedback = FeedbackMapper.toEntity(feedbackDTO);

        // Set the user
        User user = userRepository.findByUsername(feedbackDTO.getUsername())
                .orElseThrow(() -> new RecordNotFoundException("User not found: " + feedbackDTO.getUsername()));
        feedback.setUser(user);

        // Set the event
        Event event = eventRepository.findById(feedbackDTO.getEventId())
                .orElseThrow(() -> new RecordNotFoundException("Event not found: " + feedbackDTO.getEventId()));
        feedback.setEvent(event);

        // Save the feedback and return the DTO
        Feedback savedFeedback = feedbackRepository.save(feedback);
        return FeedbackMapper.toDTO(savedFeedback);
    }

    public FeedbackDTO getFeedbackById(long id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Feedback not found"));
        return FeedbackMapper.toDTO(feedback);
    }

    public FeedbackDTO updateFeedback(long id, FeedbackDTO feedbackDTO) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Feedback not found for id: " + id));

        feedback.setRating(feedbackDTO.getRating());
        feedback.setComment(feedbackDTO.getComment());

        // Set the user
        User user = userRepository.findByUsername(feedbackDTO.getUsername())
                .orElseThrow(() -> new RecordNotFoundException("User not found: " + feedbackDTO.getUsername()));
        feedback.setUser(user);

        // Set the event
        Event event = eventRepository.findById(feedbackDTO.getEventId())
                .orElseThrow(() -> new RecordNotFoundException("Event not found: " + feedbackDTO.getId()));
        feedback.setEvent(event);

        Feedback savedFeedback = feedbackRepository.save(feedback);

        return FeedbackMapper.toDTO(savedFeedback);

    }

    public void deleteFeedback(long id) {
        boolean isDeleted = feedbackRepository.existsById(id);
        if (!isDeleted) {
            throw new RecordNotFoundException("Feedback not found" + id);
        }
        feedbackRepository.deleteById(id);
    }
}
