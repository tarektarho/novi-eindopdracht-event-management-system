package nl.novi.event_management_system.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import nl.novi.event_management_system.dtos.feedbackDtos.FeedbackCreateDTO;
import nl.novi.event_management_system.dtos.feedbackDtos.FeedbackResponseDTO;
import nl.novi.event_management_system.exceptions.EventNotFoundException;
import nl.novi.event_management_system.exceptions.RecordNotFoundException;
import nl.novi.event_management_system.exceptions.ValidationException;
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
    public ResponseEntity<?> submitFeedback(@Valid @RequestBody FeedbackCreateDTO feedbackCreateDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            FeedbackResponseDTO newFeedbackDTO = feedbackService.submitFeedback(feedbackCreateDTO);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(newFeedbackDTO.getId())
                    .toUri();
            return ResponseEntity.created(location).body(newFeedbackDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFeedbackById(@PathVariable UUID id) {
        log.info("Fetching feedback with ID: {}", id);

        try {
            FeedbackResponseDTO feedbackResponseDTO = feedbackService.getFeedbackById(id);
            return ResponseEntity.ok(feedbackResponseDTO);
        } catch (RecordNotFoundException e) {
            log.warn("Feedback not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Feedback not found with ID: " + id);
        } catch (Exception e) {
            log.error("Error retrieving feedback with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving feedback.");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<FeedbackResponseDTO>> getAllFeedbacks() {
        return ResponseEntity.ok().body(feedbackService.getAllFeedbacks());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFeedback(@PathVariable UUID id, @Valid @RequestBody FeedbackCreateDTO feedbackCreateDTO) {
        log.info("Received request to update feedback with ID: {}", id);

        try {
            FeedbackResponseDTO updatedFeedback = feedbackService.updateFeedback(id, feedbackCreateDTO);
            return ResponseEntity.ok(updatedFeedback);
        } catch (RecordNotFoundException e) {
            log.warn("Feedback not found: {}", e.getMessage());
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
    public List<FeedbackResponseDTO> getFeedbackForEvent(@PathVariable UUID eventId) {
        return feedbackService.getEventFeedback(eventId);
    }

    @GetMapping("/user/{username}")
    public List<FeedbackResponseDTO> getFeedbackByUser(@PathVariable String username) {
        return feedbackService.getUserFeedback(username);
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

    @PostMapping("/{feedbackId}/event/{eventId}")
    public ResponseEntity<?> assignEventToFeedback(@PathVariable UUID feedbackId, @PathVariable UUID eventId) {
        try {
            feedbackService.assignEventToFeedback(feedbackId, eventId);
            return ResponseEntity.noContent().build();
        } catch (RecordNotFoundException | EventNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/{feedbackId}/user/{username}")
    public ResponseEntity<?> assignUserToFeedback(@PathVariable UUID feedbackId, @PathVariable String username) {
        try {
            feedbackService.assignUserToFeedback(feedbackId, username);
            return ResponseEntity.noContent().build();
        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
