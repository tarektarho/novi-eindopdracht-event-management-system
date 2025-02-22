package nl.novi.event_management_system.validators.ticketType;

import jakarta.validation.ConstraintValidatorContext;
import nl.novi.event_management_system.enums.TicketType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class TicketValidatorTest {

    private TicketValidator ticketValidator;
    private ConstraintValidatorContext context;

    @BeforeEach
    public void setUp() {
        ticketValidator = new TicketValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void testValidTicketType() {
        assertTrue(ticketValidator.isValid(TicketType.VIP, context));
        assertTrue(ticketValidator.isValid(TicketType.FREE, context));
    }

    @Test
    void testInvalidTicketType_Null() {
        assertFalse(ticketValidator.isValid(null, context)); // Null should be invalid
    }

    @Test
    void testInvalidTicketType_NotInEnum() {

        try {
            assertFalse(ticketValidator.isValid(TicketType.valueOf("EXTRA"), context));
        } catch (IllegalArgumentException e) {
            assertEquals("No enum constant nl.novi.event_management_system.enums.TicketType.EXTRA", e.getMessage());
        }
    }
}