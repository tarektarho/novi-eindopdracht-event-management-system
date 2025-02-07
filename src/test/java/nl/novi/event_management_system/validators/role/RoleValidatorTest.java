package nl.novi.event_management_system.validators.role;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class RoleValidatorTest {

    private RoleValidator roleValidator;
    private ConstraintValidatorContext context;

    @BeforeEach
    public void setUp() {
        roleValidator = new RoleValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    public void testValidRole() {
        assertTrue(roleValidator.isValid("ROLE_ADMIN", context));
        assertTrue(roleValidator.isValid("ROLE_USER", context));
    }

    @Test
    public void testInvalidRole() {
        assertFalse(roleValidator.isValid("ADMIN", context)); // Missing "ROLE_" prefix
        assertFalse(roleValidator.isValid("USER", context));  // Missing "ROLE_" prefix
    }

    @Test
    public void testNullRole() {
        assertFalse(roleValidator.isValid(null, context)); // Null role
    }

    @Test
    public void testEmptyRole() {
        assertFalse(roleValidator.isValid("", context)); // Empty role
    }

    @Test
    public void testBlankRole() {
        assertFalse(roleValidator.isValid("   ", context)); // Blank role (spaces only)
    }
}