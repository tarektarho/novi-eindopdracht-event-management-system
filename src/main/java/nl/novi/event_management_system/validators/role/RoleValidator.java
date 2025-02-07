package nl.novi.event_management_system.validators.role;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoleValidator implements ConstraintValidator<ValidRole, String> {

    @Override
    public boolean isValid(String role, ConstraintValidatorContext context) {
        if (role == null || role.isBlank()) {
            return false; // Role cannot be null or empty
        }

        return role.startsWith("ROLE_"); // Ensure it starts with "ROLE_"
    }
}
