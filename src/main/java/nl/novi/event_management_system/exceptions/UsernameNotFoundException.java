package nl.novi.event_management_system.exceptions;

import java.io.Serial;

public class UsernameNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public UsernameNotFoundException() {
        super("Username is empty");
    }

    public UsernameNotFoundException(String username) {
        super("Cannot find user " + username);
    }

}