package nl.novi.event_management_system.controllers;

import com.fasterxml.jackson.core.JsonParseException;
import nl.novi.event_management_system.dtos.ErrorResponseDTO;
import nl.novi.event_management_system.exceptions.BadRequestException;
import nl.novi.event_management_system.exceptions.RecordNotFoundException;
import nl.novi.event_management_system.exceptions.UsernameNotFoundException;
import nl.novi.event_management_system.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@ControllerAdvice
public class GlobalExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles RecordNotFoundException and returns a 404 Not Found response.
     *
     * @param recordNotFoundException the exception to handle
     * @return a ResponseEntity containing the error response
     */
    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponseDTO> handleNotFoundException(RecordNotFoundException recordNotFoundException) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                recordNotFoundException.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        logger.warn(errorResponse.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles BadRequestException and returns a 400 Bad Request response.
     *
     * @param badRequestException the exception to handle
     * @return a ResponseEntity containing the error response
     */
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDTO> handleBadRequestException(BadRequestException badRequestException) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                badRequestException.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        logger.warn(errorResponse.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles InternalAuthenticationServiceException and returns a 401 Unauthorized response.
     *
     * @param internalAuthenticationServiceException the exception to handle
     * @return a ResponseEntity containing the error response
     */
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFoundException(InternalAuthenticationServiceException internalAuthenticationServiceException) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                internalAuthenticationServiceException.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles IllegalArgumentException and returns a 400 Bad Request response.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity containing the error message
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        logger.warn(ex.getMessage());
        return ResponseEntity.badRequest().body("Invalid input: " + ex.getMessage());
    }

    /**
     * Handles IOException and returns a 500 Internal Server Error response.
     *
     * @param exception the exception to handle
     * @return a ResponseEntity containing the error message
     */
    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<String> exception(IOException exception) {
        String message = "Problem with file storage" + exception.getMessage();
        logger.warn(message);
        return ResponseEntity.internalServerError().body(message);
    }

    /**
     * Handles generic Exception and returns a 500 Internal Server Error response.
     *
     * @param exception the exception to handle
     * @return a ResponseEntity containing the error message
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> exception(Exception exception) {
        String message = "An unexpected error occurred: " + exception.getMessage();
        logger.error(message);
        return ResponseEntity.internalServerError().body(message);
    }

    /**
     * Handles ValidationException and returns a 400 Bad Request response.
     *
     * @param exception the exception to handle
     * @return a ResponseEntity containing the error message
     */
    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException exception) {
        logger.warn(exception.getMessage());
        return ResponseEntity.badRequest().body("Validation failed: " + exception.getMessage());
    }

    /**
     * Handles HttpMessageNotReadableException and returns a 400 Bad Request response.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity containing the error response
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDTO> handleJsonParseException(HttpMessageNotReadableException ex) {
        String errorMessage = "Invalid request body. Please check JSON syntax and data types.";

        if (ex.getRootCause() instanceof JsonParseException) {
            errorMessage = "Invalid JSON format. Ensure your request body is properly structured.";
        } else if (ex.getRootCause() instanceof DateTimeParseException) {
            errorMessage = "Invalid date format. Please use 'YYYY-MM-DD'.";
        }

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                errorMessage,
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        logger.error("JSON parse error: {}", errorMessage);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handles UsernameNotFoundException and returns a 404 Not Found response.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity containing the error response
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUsernameNotFound(UsernameNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}