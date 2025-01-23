package nl.novi.event_management_system.dtos;

import nl.novi.event_management_system.enums.ConfirmationStatus;
import nl.novi.event_management_system.models.Event;

public class ReservationDTO {

    private Long id;                        // Reservation ID
    private String username;                // Username of the user making the reservation
    private Event event;                 // Event associated with the reservation
    private String details;                 // Details of the reservation
    private ConfirmationStatus confirmationStatus; // Confirmation status (e.g., CONFIRMED, PENDING, etc.)

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public ConfirmationStatus getConfirmationStatus() {
        return confirmationStatus;
    }

    public void setConfirmationStatus(ConfirmationStatus confirmationStatus) {
        this.confirmationStatus = confirmationStatus;
    }
}
