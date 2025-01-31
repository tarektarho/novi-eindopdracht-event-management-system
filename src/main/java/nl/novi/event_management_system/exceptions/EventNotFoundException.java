package nl.novi.event_management_system.exceptions;

import java.util.UUID;

public class EventNotFoundException extends RuntimeException {

    public EventNotFoundException() {
        super("EventId is empty.");
    }

    public EventNotFoundException(String message) {
        super(message);
    }

    public EventNotFoundException(UUID id) {
        super("Event not found. EventId: " + id);
    }
}
