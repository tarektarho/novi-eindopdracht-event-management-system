package nl.novi.event_management_system.validators.role;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import nl.novi.event_management_system.models.Role;

import java.util.Set;

public class RoleValidator implements ConstraintValidator<ValidRole, Set<Role>> {
    @Override
    public boolean isValid(Set<Role> roles, ConstraintValidatorContext context) {
        if (roles == null || roles.isEmpty()) {
            return true; // Null or empty roles are considered valid
        }

        // Validate each role to ensure it starts with "ROLE_"
        for (Role role : roles) {
            if (role.getRole() == null || !role.getRole().startsWith("ROLE_")) {
                return false;
            }
        }

        return true;
    }
}
