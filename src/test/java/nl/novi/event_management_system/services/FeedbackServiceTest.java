package nl.novi.event_management_system.services;

import nl.novi.event_management_system.dtos.feedbackDtos.FeedbackCreateDTO;
import nl.novi.event_management_system.dtos.feedbackDtos.FeedbackResponseDTO;
import nl.novi.event_management_system.exceptions.RecordNotFoundException;
import nl.novi.event_management_system.models.Event;
import nl.novi.event_management_system.models.Feedback;
import nl.novi.event_management_system.models.User;
import nl.novi.event_management_system.repositories.EventRepository;
import nl.novi.event_management_system.repositories.FeedbackRepository;
import nl.novi.event_management_system.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private FeedbackService feedbackService;

    private FeedbackCreateDTO feedbackCreateDTO;
    private Feedback feedback;

    @BeforeEach
    void setUp() {
        feedbackCreateDTO = new FeedbackCreateDTO();
        feedbackCreateDTO.setUsername("testUser");
        feedbackCreateDTO.setComment("Great event!");
        feedbackCreateDTO.setRating(5);
        feedbackCreateDTO.setEventId(UUID.randomUUID());

        feedback = new Feedback();
        feedback.setComment("Great event!");
        feedback.setRating(5);
    }

    @Test
    void submitFeedback_ShouldReturnFeedbackResponseDTO() {
        User user = new User();
        user.setUsername(feedbackCreateDTO.getUsername());

        Event event = new Event();
        event.setId(feedbackCreateDTO.getEventId());

        // Arrange
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(eventRepository.findById(feedbackCreateDTO.getEventId())).thenReturn(Optional.of(event));
        when(feedbackRepository.save(any(Feedback.class))).thenAnswer(invocation -> {
            Feedback savedFeedback = invocation.getArgument(0);
            savedFeedback.setId(UUID.randomUUID());
            return savedFeedback;
        });

        // Act
        FeedbackResponseDTO response = feedbackService.submitFeedback(feedbackCreateDTO);

        // Assert
        assertEquals(feedbackCreateDTO.getComment(), response.getComment());
        assertEquals(feedbackCreateDTO.getRating(), response.getRating());
        assertEquals(feedbackCreateDTO.getUsername(), response.getUsername());
        assertEquals(feedbackCreateDTO.getEventId(), response.getEventId());

        // Verify that feedback was saved
        ArgumentCaptor<Feedback> feedbackCaptor = ArgumentCaptor.forClass(Feedback.class);
        verify(feedbackRepository).save(feedbackCaptor.capture());

        Feedback capturedFeedback = feedbackCaptor.getValue();
        assertEquals(feedbackCreateDTO.getComment(), capturedFeedback.getComment());
        assertEquals(feedbackCreateDTO.getRating(), capturedFeedback.getRating());

        verify(feedbackRepository, times(1)).save(any(Feedback.class));
    }

    @Test
    void getFeedbackById_ShouldReturnFeedbackResponseDTO() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(feedbackRepository.findById(id)).thenReturn(Optional.of(feedback));

        // Act
        FeedbackResponseDTO response = feedbackService.getFeedbackById(id);

        // Assert
        assertEquals(feedback.getComment(), response.getComment());
        assertEquals(feedback.getRating(), response.getRating());

        verify(feedbackRepository, times(1)).findById(id);
    }

    @Test
    void getFeedbackById_ShouldThrowRecordNotFoundException() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(feedbackRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RecordNotFoundException.class, () -> feedbackService.getFeedbackById(id));

        verify(feedbackRepository, times(1)).findById(id);
    }

    @Test
    void getAllFeedbacks_ShouldReturnListOfFeedbackResponseDTO() {
        // Arrange
        when(feedbackRepository.findAll()).thenReturn(List.of(feedback));

        // Act
        var response = feedbackService.getAllFeedbacks();

        // Assert
        assertEquals(1, response.size());
        assertEquals(feedback.getComment(), response.get(0).getComment());
        assertEquals(feedback.getRating(), response.get(0).getRating());

        verify(feedbackRepository, times(1)).findAll();
    }

    @Test
    void getAllFeedbacks_ShouldReturnEmptyList() {
        // Arrange
        when(feedbackRepository.findAll()).thenReturn(List.of());

        // Act
        var response = feedbackService.getAllFeedbacks();

        // Assert
        assertEquals(0, response.size());

        verify(feedbackRepository, times(1)).findAll();
    }

    @Test
    void updateFeedback_ShouldReturnFeedbackResponseDTO() {
        // Arrange
        UUID id = UUID.randomUUID();
        feedback.setUser(new User());
        when(feedbackRepository.findById(id)).thenReturn(Optional.of(feedback));
        when(feedbackRepository.save(any(Feedback.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FeedbackCreateDTO updatedFeedback = new FeedbackCreateDTO();
        updatedFeedback.setComment("Updated comment");
        updatedFeedback.setRating(4);


        // Act
        FeedbackResponseDTO response = feedbackService.updateFeedback(id, updatedFeedback);

        // Assert
        assertEquals(updatedFeedback.getComment(), response.getComment());
        assertEquals(updatedFeedback.getRating(), response.getRating());

        // Verify that feedback was updated
        ArgumentCaptor<Feedback> feedbackCaptor = ArgumentCaptor.forClass(Feedback.class);
        verify(feedbackRepository).save(feedbackCaptor.capture());

        Feedback capturedFeedback = feedbackCaptor.getValue();
        assertEquals(updatedFeedback.getComment(), capturedFeedback.getComment());
        assertEquals(updatedFeedback.getRating(), capturedFeedback.getRating());

        verify(feedbackRepository, times(1)).findById(id);
        verify(feedbackRepository, times(1)).save(any(Feedback.class));
    }

    @Test
    void updateFeedback_ShouldThrowRecordNotFoundException() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(feedbackRepository.findById(id)).thenReturn(Optional.empty());

        FeedbackCreateDTO updatedFeedback = new FeedbackCreateDTO();
        updatedFeedback.setComment("Updated comment");
        updatedFeedback.setRating(4);

        // Act and Assert
        assertThrows(RecordNotFoundException.class, () -> feedbackService.updateFeedback(id, updatedFeedback));

        verify(feedbackRepository, times(1)).findById(id);
        verify(feedbackRepository, never()).save(any(Feedback.class));
    }

    @Test
    void updateFeedback_ShouldThrowRecordNotFoundExceptionForUser() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(feedbackRepository.findById(id)).thenReturn(Optional.empty());

        FeedbackCreateDTO updatedFeedback = new FeedbackCreateDTO();
        updatedFeedback.setComment("Updated comment");
        updatedFeedback.setRating(4);

        // Act and Assert
        assertThrows(RecordNotFoundException.class, () -> feedbackService.updateFeedback(id, updatedFeedback));

        verify(feedbackRepository, times(1)).findById(id);
        verify(feedbackRepository, never()).save(any(Feedback.class));
    }

    @Test
    void deleteFeedback_ShouldDeleteFeedback() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(feedbackRepository.findById(id)).thenReturn(Optional.of(feedback));

        // Act
        feedbackService.deleteFeedback(id);

        // Assert
        verify(feedbackRepository, times(1)).delete(feedback);
    }

    @Test
    void deleteFeedback_ShouldThrowRecordNotFoundException() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(feedbackRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RecordNotFoundException.class, () -> feedbackService.deleteFeedback(id));

        verify(feedbackRepository, never()).delete(any(Feedback.class));
    }

    @Test
    void getEventFeedback_ShouldReturnListOfFeedbackResponseDTO() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        when(feedbackRepository.findByEventId(eventId)).thenReturn(List.of(feedback));

        // Act
        var response = feedbackService.getEventFeedback(eventId);

        // Assert
        assertEquals(1, response.size());
        assertEquals(feedback.getComment(), response.getFirst().getComment());
        assertEquals(feedback.getRating(), response.getFirst().getRating());

        verify(feedbackRepository, times(1)).findByEventId(eventId);
    }

    @Test
    void getUserFeedback_ShouldReturnListOfFeedbackResponseDTO() {
        // Arrange
        String username = "testUser";
        when(feedbackRepository.findByUserUsername(username)).thenReturn(List.of(feedback));

        // Act
        var response = feedbackService.getUserFeedback(username);

        // Assert
        assertEquals(1, response.size());
        assertEquals(feedback.getComment(), response.getFirst().getComment());
        assertEquals(feedback.getRating(), response.getFirst().getRating());

        verify(feedbackRepository, times(1)).findByUserUsername(username);
    }
}
