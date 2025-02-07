package nl.novi.event_management_system.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordGeneratorTest {

    @Test
    void generateEncryptedPassword() {
        String password = "password";
        String encryptedPassword = PasswordGenerator.generateEncryptedPassword(password);
        assertNotEquals(password, encryptedPassword);
    }

}