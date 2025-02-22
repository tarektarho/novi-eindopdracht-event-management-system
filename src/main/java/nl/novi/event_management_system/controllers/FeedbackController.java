package nl.novi.event_management_system.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.novi.event_management_system.dtos.feedbackDtos.FeedbackCreateDTO;
import nl.novi.event_management_system.dtos.feedbackDtos.FeedbackResponseDTO;
import nl.novi.event_management_system.services.FeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Tag(name = "Feedback API", description = "Feedback related endpoints")
@RestController
@RequestMapping("/api/v1/feedback")
public class FeedbackController {
    Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    /**
     * Submit a new feedback
     *
     * @param feedbackCreateDTO FeedbackCreateDTO
     * @return ResponseEntity<?>
     */
    @PostMapping
    public ResponseEntity<?> submitFeedback(@Valid @RequestBody FeedbackCreateDTO feedbackCreateDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }

        FeedbackResponseDTO newFeedbackDTO = feedbackService.submitFeedback(feedbackCreateDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newFeedbackDTO.getId())
                .toUri();

        return ResponseEntity.created(location).body(newFeedbackDTO);
    }

    /**
     * Get feedback by ID
     *
     * @param id UUID
     * @return ResponseEntity<FeedbackResponseDTO>
     */
    @GetMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> getFeedbackById(@PathVariable UUID id) {
        log.info("Fetching feedback with ID: {}", id);
        return ResponseEntity.ok(feedbackService.getFeedbackById(id));
    }

    /**
     * Get all feedbacks
     *
     * @return ResponseEntity<List < FeedbackResponseDTO>>
     */
    @GetMapping
    public ResponseEntity<List<FeedbackResponseDTO>> getAllFeedbacks() {
        return ResponseEntity.ok().body(feedbackService.getAllFeedbacks());
    }

    /**
     * Update feedback by ID
     *
     * @param id                UUID
     * @param feedbackCreateDTO FeedbackCreateDTO
     * @return ResponseEntity<FeedbackResponseDTO>
     */
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> updateFeedback(@PathVariable UUID id, @Valid @RequestBody FeedbackCreateDTO feedbackCreateDTO) {
        log.info("Received request to update feedback with ID: {}", id);
        return ResponseEntity.ok(feedbackService.updateFeedback(id, feedbackCreateDTO));
    }

    /**
     * Delete feedback by ID
     *
     * @param eventId UUID
     * @return ResponseEntity<Void>
     */
    @GetMapping("/event/{eventId}")
    public List<FeedbackResponseDTO> getFeedbackForEvent(@PathVariable UUID eventId) {
        return feedbackService.getEventFeedback(eventId);
    }

    /**
     * Get feedback by user
     *
     * @param username String
     * @return List<FeedbackResponseDTO>
     */
    @GetMapping("/user/{username}")
    public List<FeedbackResponseDTO> getFeedbackByUser(@PathVariable String username) {
        return feedbackService.getUserFeedback(username);
    }

    /**
     * Delete feedback by ID
     *
     * @param id UUID
     * @return ResponseEntity<Void>
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable UUID id) {
        log.info("Received request to delete feedback with ID: {}", id);
        feedbackService.deleteFeedback(id);
        log.info("Successfully deleted feedback with ID: {}", id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
