package nl.novi.event_management_system.dtos.feedbackDtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class FeedbackCreateDTO {
    @Schema(description = "The comment of the feedback.", example = "The event was great!")
    @NotNull(message = "comment cannot be empty.")
    private String comment;
    @Schema(description = "The rating of the feedback.", example = "5")
    @NotNull(message = "rating cannot be empty.")
    private int rating;
    @Schema(description = "The username of the user who gave the feedback.", example = "jack")
    @NotNull(message = "username cannot be empty.")
    private String username;
    @Schema(description = "The ID of the event this feedback is for.", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "eventId cannot be empty.")
    private UUID eventId;

    // Getters and Setters
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }
}
