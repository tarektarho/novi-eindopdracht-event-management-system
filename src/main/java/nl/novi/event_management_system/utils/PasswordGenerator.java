package nl.novi.event_management_system.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {


    /**
     * Generates an encrypted password using BCrypt hashing algorithm.
     *
     * @param password the plain text password to be encrypted
     * @return the encrypted password
     */
    public static String generateEncryptedPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        return encoder.encode(password);
    }

}
