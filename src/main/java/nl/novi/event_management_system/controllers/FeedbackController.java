package nl.novi.event_management_system.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.novi.event_management_system.models.Feedback;
import nl.novi.event_management_system.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@Tag(name = "Feedback API", description = "Feedback related endpoints")
@RestController
@RequestMapping("/api/v1/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/submit")
    public Feedback submitFeedback(
            @RequestParam String username,
            @RequestParam UUID eventId,
            @RequestParam int rating,
            @RequestParam String comment) {
        return feedbackService.submitFeedback(username, eventId, rating, comment);
    }

    @GetMapping("/event/{eventId}")
    public List<Feedback> getFeedbackForEvent(@PathVariable UUID eventId) {
        return feedbackService.getFeedbackForEvent(eventId);
    }

    @GetMapping("/user/{username}")
    public List<Feedback> getFeedbackByUser(@PathVariable String username) {
        return feedbackService.getFeedbackByUser(username);
    }
}
