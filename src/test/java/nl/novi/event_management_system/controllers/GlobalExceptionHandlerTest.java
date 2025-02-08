package nl.novi.event_management_system.controllers;

import com.fasterxml.jackson.core.JsonParseException;
import nl.novi.event_management_system.dtos.ErrorResponseDTO;
import nl.novi.event_management_system.exceptions.BadRequestException;
import nl.novi.event_management_system.exceptions.RecordNotFoundException;
import nl.novi.event_management_system.exceptions.UsernameNotFoundException;
import nl.novi.event_management_system.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleRecordNotFoundException() {
        // Arrange
        String errorMessage = "Record not found";
        RecordNotFoundException exception = new RecordNotFoundException(errorMessage);

        // Act
        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.handleNotFoundException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorResponseDTO errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(errorMessage, errorResponse.getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getStatusCode());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void testHandleInternalAuthenticationServiceException() {
        // Arrange
        String errorMessage = "Authentication failed";
        InternalAuthenticationServiceException exception = new InternalAuthenticationServiceException(errorMessage);

        // Act
        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.handleNotFoundException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        ErrorResponseDTO errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(errorMessage, errorResponse.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED.value(), errorResponse.getStatusCode());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void testHandleIllegalArgumentException() {
        // Arrange
        String errorMessage = "Invalid input";
        IllegalArgumentException exception = new IllegalArgumentException(errorMessage);

        // Act
        ResponseEntity<String> response = globalExceptionHandler.handleIllegalArgument(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid input: " + errorMessage, response.getBody());
    }

    @Test
    void testHandleIOException() {
        // Arrange
        String errorMessage = "Problem with file storage";
        IOException exception = new IOException(errorMessage);

        // Act
        ResponseEntity<String> response = globalExceptionHandler.exception(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Problem with file storage" + errorMessage, response.getBody());
    }

    @Test
    void testHandleException() {
        // Arrange
        String errorMessage = "An unexpected error occurred";
        Exception exception = new Exception(errorMessage);

        // Act
        ResponseEntity<String> response = globalExceptionHandler.exception(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred: " + errorMessage, response.getBody());
    }

    @Test
    void testHandleValidationException() {
        // Arrange
        String errorMessage = "Validation failed";
        ValidationException exception = new ValidationException(errorMessage);

        // Act
        ResponseEntity<String> response = globalExceptionHandler.handleValidationException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation failed: " + errorMessage, response.getBody());
    }

    @Test
    void testHandleBadRequestException() {
        // Arrange
        String errorMessage = "Bad request";
        BadRequestException exception = new BadRequestException(errorMessage);

        // Act
        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.handleBadRequestException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorResponseDTO errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(errorMessage, errorResponse.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatusCode());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void testHandleJsonParseException() {
        // Arrange
        String errorMessage = "Invalid JSON format. Ensure your request body is properly structured.";
        JsonParseException jsonParseException = new JsonParseException("Malformed JSON");
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Request body error", jsonParseException);

        // Act
        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.handleJsonParseException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorResponseDTO errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(errorMessage, errorResponse.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatusCode());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void testHandleJsonParseExceptionInvalidDateFormat() {
        // Arrange
        String errorMessage = "Invalid date format. Please use 'YYYY-MM-DD'.";
        DateTimeParseException dateTimeParseException = new DateTimeParseException("Invalid date format", "2021-13-01", 10);
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Request body error", dateTimeParseException);

        // Act
        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.handleJsonParseException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorResponseDTO errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(errorMessage, errorResponse.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatusCode());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void testHandleUsernameNotFoundException() {

        // Arrange
        String errorMessage = "Username is empty";
        UsernameNotFoundException exception = new UsernameNotFoundException();

        // Act
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleUsernameNotFound(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(errorMessage, response.getBody().get("error"));
    }


}