package nl.novi.event_management_system.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import nl.novi.event_management_system.dtos.FeedbackDTO;
import nl.novi.event_management_system.exceptions.RecordNotFoundException;
import nl.novi.event_management_system.exceptions.ValidationException;
import nl.novi.event_management_system.models.Feedback;
import nl.novi.event_management_system.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@Tag(name = "Feedback API", description = "Feedback related endpoints")
@RestController
@RequestMapping("/api/v1/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/submit")
    public ResponseEntity<?> submitFeedback(@Valid @RequestBody FeedbackDTO feedbackDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            FeedbackDTO newFeedbackDTO = feedbackService.submitFeedback(feedbackDTO);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(newFeedbackDTO.getId())
                    .toUri();
            return ResponseEntity.created(location).body(newFeedbackDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error submitting feedback: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFeedbackById(@PathVariable UUID id) {
        log.info("Fetching feedback with ID: {}", id);

        try {
            FeedbackDTO feedbackDTO = feedbackService.getFeedbackById(id);
            return ResponseEntity.ok(feedbackDTO);
        } catch (RecordNotFoundException e) {
            log.warn("Feedback not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Feedback not found with ID: " + id);
        } catch (Exception e) {
            log.error("Error retrieving feedback with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving feedback.");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<FeedbackDTO>> getAllFeedbacks() {
        return ResponseEntity.ok().body(feedbackService.getAllFeedbacks());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFeedback(@PathVariable UUID id, @Valid @RequestBody FeedbackDTO feedbackDTO) {
        log.info("Received request to update feedback with ID: {}", id);

        try {
            FeedbackDTO updatedFeedback = feedbackService.updateFeedback(id, feedbackDTO);
            return ResponseEntity.ok(updatedFeedback);
        } catch (RecordNotFoundException e) {
            log.warn("Feedback or user not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ValidationException e) {
            log.warn("Validation error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Validation failed: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while updating feedback: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating feedback. Please try again later.");
        }
    }

    @GetMapping("/event/{eventId}")
    public List<Feedback> getFeedbackForEvent(@PathVariable UUID eventId) {
        return feedbackService.getFeedbackForEvent(eventId);
    }

    @GetMapping("/user/{username}")
    public List<Feedback> getFeedbackByUser(@PathVariable String username) {
        return feedbackService.getFeedbackByUser(username);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeedback(@PathVariable UUID id) {
        log.info("Received request to delete feedback with ID: {}", id);

        try {
            feedbackService.deleteFeedback(id);
            log.info("Successfully deleted feedback with ID: {}", id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (RecordNotFoundException e) {
            log.warn("Feedback not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Feedback not found with ID: " + id);
        } catch (Exception e) {
            log.error("Error deleting feedback with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting feedback. Please try again later.");
        }
    }
}
