package nl.novi.event_management_system.controllers;

import nl.novi.event_management_system.dtos.feedbackDtos.FeedbackCreateDTO;
import nl.novi.event_management_system.dtos.feedbackDtos.FeedbackResponseDTO;
import nl.novi.event_management_system.services.FeedbackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FeedbackControllerTest {

    @Mock
    private FeedbackService feedbackService;

    @InjectMocks
    private FeedbackController feedbackController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSubmitFeedback_ValidInput() {
        // Arrange
        FeedbackCreateDTO feedbackCreateDTO = new FeedbackCreateDTO();
        FeedbackResponseDTO feedbackResponseDTO = new FeedbackResponseDTO();
        feedbackResponseDTO.setId(UUID.randomUUID());
        feedbackResponseDTO.setComment("Test comment");
        feedbackResponseDTO.setRating(5);

        when(feedbackService.submitFeedback(any(FeedbackCreateDTO.class))).thenReturn(feedbackResponseDTO);

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        // Mock HTTP request context
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // Act
        ResponseEntity<?> response = feedbackController.submitFeedback(feedbackCreateDTO, bindingResult);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(feedbackResponseDTO, response.getBody());
    }

    @Test
    void testSubmitFeedback_InvalidInput() {
        // Arrange
        FeedbackCreateDTO feedbackCreateDTO = new FeedbackCreateDTO();
        FeedbackResponseDTO feedbackResponseDTO = new FeedbackResponseDTO();
        feedbackResponseDTO.setId(UUID.randomUUID());

        when(feedbackService.submitFeedback(any(FeedbackCreateDTO.class))).thenReturn(feedbackResponseDTO);

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        // Mock HTTP request context
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // Act
        ResponseEntity<?> response = feedbackController.submitFeedback(feedbackCreateDTO, bindingResult);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetFeedbackById_Success() {
        // Arrange
        UUID feedbackId = UUID.randomUUID();
        FeedbackResponseDTO feedbackResponseDTO = new FeedbackResponseDTO();
        feedbackResponseDTO.setId(feedbackId);

        when(feedbackService.getFeedbackById(feedbackId)).thenReturn(feedbackResponseDTO);

        // Act
        ResponseEntity<FeedbackResponseDTO> response = feedbackController.getFeedbackById(feedbackId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(feedbackResponseDTO, response.getBody());
    }

    @Test
    void testGetAllFeedbacks() {
        // Arrange
        List<FeedbackResponseDTO> feedbackList = Collections.singletonList(new FeedbackResponseDTO());
        when(feedbackService.getAllFeedbacks()).thenReturn(feedbackList);

        // Act
        ResponseEntity<List<FeedbackResponseDTO>> response = feedbackController.getAllFeedbacks();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(feedbackList, response.getBody());
    }

    @Test
    void testUpdateFeedback_Success() {
        // Arrange
        UUID feedbackId = UUID.randomUUID();
        FeedbackCreateDTO feedbackCreateDTO = new FeedbackCreateDTO();
        FeedbackResponseDTO updatedFeedback = new FeedbackResponseDTO();
        updatedFeedback.setId(feedbackId);

        when(feedbackService.updateFeedback(feedbackId, feedbackCreateDTO)).thenReturn(updatedFeedback);

        // Act
        ResponseEntity<FeedbackResponseDTO> response = feedbackController.updateFeedback(feedbackId, feedbackCreateDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedFeedback, response.getBody());
    }

    @Test
    void testGetFeedbackForEvent() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        List<FeedbackResponseDTO> feedbackList = Collections.singletonList(new FeedbackResponseDTO());

        when(feedbackService.getEventFeedback(eventId)).thenReturn(feedbackList);

        // Act
        List<FeedbackResponseDTO> response = feedbackController.getFeedbackForEvent(eventId);

        // Assert
        assertEquals(feedbackList, response);
    }

    @Test
    void testGetFeedbackByUser() {
        // Arrange
        String username = "testUser";
        List<FeedbackResponseDTO> feedbackList = Collections.singletonList(new FeedbackResponseDTO());

        when(feedbackService.getUserFeedback(username)).thenReturn(feedbackList);

        // Act
        List<FeedbackResponseDTO> response = feedbackController.getFeedbackByUser(username);

        // Assert
        assertEquals(feedbackList, response);
    }

    @Test
    void testDeleteFeedback_Success() {
        // Arrange
        UUID feedbackId = UUID.randomUUID();
        doNothing().when(feedbackService).deleteFeedback(feedbackId);

        // Act
        ResponseEntity<Void> response = feedbackController.deleteFeedback(feedbackId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(feedbackService, times(1)).deleteFeedback(feedbackId);
    }
}