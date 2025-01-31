package nl.novi.event_management_system.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("Email is already exists Email: " + email);
    }
}
