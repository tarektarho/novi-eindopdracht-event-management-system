package nl.novi.event_management_system.controllers;

import nl.novi.event_management_system.dtos.feedbackDtos.FeedbackCreateDTO;
import nl.novi.event_management_system.dtos.feedbackDtos.FeedbackResponseDTO;
import nl.novi.event_management_system.exceptions.EventNotFoundException;
import nl.novi.event_management_system.exceptions.RecordNotFoundException;
import nl.novi.event_management_system.exceptions.ValidationException;
import nl.novi.event_management_system.services.FeedbackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "admin", roles = {"ADMIN"})
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
        UUID feedbackId = UUID.randomUUID();
        feedbackResponseDTO.setId(feedbackId);

        when(feedbackService.submitFeedback(any(FeedbackCreateDTO.class))).thenReturn(feedbackResponseDTO);

        // Mock ServletUriComponentsBuilder
        try (MockedStatic<ServletUriComponentsBuilder> mockedBuilder = mockStatic(ServletUriComponentsBuilder.class)) {
            ServletUriComponentsBuilder builder = mock(ServletUriComponentsBuilder.class);
            UriComponents uriComponents = mock(UriComponents.class);

            when(ServletUriComponentsBuilder.fromCurrentRequest()).thenReturn(builder);
            when(builder.path(anyString())).thenReturn(builder);
            when(builder.buildAndExpand(feedbackId)).thenReturn(uriComponents);
            when(uriComponents.toUri()).thenReturn(new URI("http://localhost/api/v1/feedback/" + feedbackId));

            // Act
            ResponseEntity<?> response = feedbackController.submitFeedback(feedbackCreateDTO, mock(BindingResult.class));

            // Assert
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals(feedbackResponseDTO, response.getBody());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    void testSubmitFeedback_Exception() {
        // Arrange
        FeedbackCreateDTO feedbackCreateDTO = new FeedbackCreateDTO();
        when(feedbackService.submitFeedback(any(FeedbackCreateDTO.class))).thenThrow(new RuntimeException("Unexpected error"));

        // Act
        ResponseEntity<?> response = feedbackController.submitFeedback(feedbackCreateDTO, mock(BindingResult.class));

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Unexpected error", response.getBody());
    }

    @Test
    void testSubmitFeedback_ReturnsBadRequest() {
        // Arrange
        FeedbackCreateDTO feedbackCreateDTO = new FeedbackCreateDTO();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        // Act
        ResponseEntity<?> response = feedbackController.submitFeedback(feedbackCreateDTO, result);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
    }

    @Test
    void testGetFeedbackById_Success() throws RecordNotFoundException {
        // Arrange
        UUID id = UUID.randomUUID();
        FeedbackResponseDTO feedbackResponseDTO = new FeedbackResponseDTO();
        feedbackResponseDTO.setId(id);

        when(feedbackService.getFeedbackById(id)).thenReturn(feedbackResponseDTO);

        // Act
        ResponseEntity<?> response = feedbackController.getFeedbackById(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(feedbackResponseDTO, response.getBody());
    }

    @Test
    void testGetFeedbackById_NotFound() throws RecordNotFoundException {
        // Arrange
        UUID id = UUID.randomUUID();
        when(feedbackService.getFeedbackById(id)).thenThrow(new RecordNotFoundException("Feedback not found"));

        // Act
        ResponseEntity<?> response = feedbackController.getFeedbackById(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Feedback not found with ID: " + id, response.getBody());
    }

    @Test
    void testGetFeedbackById_ReturnsInternalServerError() throws RecordNotFoundException {
        // Arrange
        UUID id = UUID.randomUUID();
        when(feedbackService.getFeedbackById(id)).thenThrow(new RuntimeException("Unexpected error"));

        // Act
        ResponseEntity<?> response = feedbackController.getFeedbackById(id);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(feedbackController.FETCH_FEEDBACK_ERROR_MESSAGE, response.getBody());
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
    void testUpdateFeedback_Success() throws RecordNotFoundException, ValidationException {
        // Arrange
        UUID id = UUID.randomUUID();
        FeedbackCreateDTO feedbackCreateDTO = new FeedbackCreateDTO();
        FeedbackResponseDTO updatedFeedback = new FeedbackResponseDTO();
        updatedFeedback.setId(id);

        when(feedbackService.updateFeedback(id, feedbackCreateDTO)).thenReturn(updatedFeedback);

        // Act
        ResponseEntity<?> response = feedbackController.updateFeedback(id, feedbackCreateDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedFeedback, response.getBody());
    }

    @Test
    void testUpdateFeedback_ValidationException() throws RecordNotFoundException, ValidationException {
        // Arrange
        UUID id = UUID.randomUUID();
        FeedbackCreateDTO feedbackCreateDTO = new FeedbackCreateDTO();
        FeedbackResponseDTO updatedFeedback = new FeedbackResponseDTO();
        updatedFeedback.setId(id);

        when(feedbackService.updateFeedback(id, feedbackCreateDTO)).thenThrow(new ValidationException("Validation failed"));

        // Act
        ResponseEntity<?> response = feedbackController.updateFeedback(id, feedbackCreateDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation failed: Validation failed", response.getBody());
    }

    @Test
    void testUpdateFeedback_NotFound() throws RecordNotFoundException, ValidationException {
        // Arrange
        UUID id = UUID.randomUUID();
        FeedbackCreateDTO feedbackCreateDTO = new FeedbackCreateDTO();

        when(feedbackService.updateFeedback(id, feedbackCreateDTO)).thenThrow(new RecordNotFoundException("Feedback not found"));

        // Act
        ResponseEntity<?> response = feedbackController.updateFeedback(id, feedbackCreateDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Feedback not found", response.getBody());
    }

    @Test
    void testUpdateFeedback_ReturnsInternalServerError() throws RecordNotFoundException, ValidationException {
        // Arrange
        UUID id = UUID.randomUUID();
        FeedbackCreateDTO feedbackCreateDTO = new FeedbackCreateDTO();

        when(feedbackService.updateFeedback(id, feedbackCreateDTO)).thenThrow(new RuntimeException("Unexpected error"));

        // Act
        ResponseEntity<?> response = feedbackController.updateFeedback(id, feedbackCreateDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error updating feedback. Please try again later.", response.getBody());
    }

    @Test
    void testDeleteFeedback_Success() throws RecordNotFoundException {
        // Arrange
        UUID id = UUID.randomUUID();
        doNothing().when(feedbackService).deleteFeedback(id);

        // Act
        ResponseEntity<?> response = feedbackController.deleteFeedback(id);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteFeedback_NotFound() throws RecordNotFoundException {
        // Arrange
        UUID id = UUID.randomUUID();
        doThrow(new RecordNotFoundException("Feedback not found")).when(feedbackService).deleteFeedback(id);

        // Act
        ResponseEntity<?> response = feedbackController.deleteFeedback(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Feedback not found with ID: " + id, response.getBody());
    }

    @Test
    void testAssignEventToFeedback_Success() throws RecordNotFoundException, EventNotFoundException {
        // Arrange
        UUID feedbackId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        doNothing().when(feedbackService).assignEventToFeedback(feedbackId, eventId);

        // Act
        ResponseEntity<?> response = feedbackController.assignEventToFeedback(feedbackId, eventId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testAssignEventToFeedback_NotFound() throws RecordNotFoundException, EventNotFoundException {
        // Arrange
        UUID feedbackId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        doThrow(new RecordNotFoundException("Feedback not found")).when(feedbackService).assignEventToFeedback(feedbackId, eventId);

        // Act
        ResponseEntity<?> response = feedbackController.assignEventToFeedback(feedbackId, eventId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Feedback not found", response.getBody());
    }

    @Test
    void testAssignUserToFeedback_Success() throws RecordNotFoundException {
        // Arrange
        UUID feedbackId = UUID.randomUUID();
        String username = "testUser";
        doNothing().when(feedbackService).assignUserToFeedback(feedbackId, username);

        // Act
        ResponseEntity<?> response = feedbackController.assignUserToFeedback(feedbackId, username);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testAssignUserToFeedback_NotFound() throws RecordNotFoundException {
        // Arrange
        UUID feedbackId = UUID.randomUUID();
        String username = "testUser";
        doThrow(new RecordNotFoundException("Feedback not found")).when(feedbackService).assignUserToFeedback(feedbackId, username);

        // Act
        ResponseEntity<?> response = feedbackController.assignUserToFeedback(feedbackId, username);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Feedback not found", response.getBody());
    }
}