package nl.novi.event_management_system.dtos.feedbackDtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public class FeedbackCreateDTO {
    private String comment;
    private int rating;
    @Schema(hidden = true)
    private String username;
    @Schema(hidden = true)
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
