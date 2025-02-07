package nl.novi.event_management_system.validators.role;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * Custom validation annotation for Role validation.
 * Ensures that each role starts with 'ROLE_' (e.g., ROLE_ADMIN).
 */
@Documented
@Constraint(validatedBy = RoleValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER}) // Can be used on fields and method parameters
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRole {

    /**
     * Default error message when validation fails.
     */
    String message() default "Invalid role. Each role must start with 'ROLE_' (e.g., ROLE_ADMIN)";

    /**
     * Used for grouping constraints.
     */
    Class<?>[] groups() default {};

    /**
     * Used to specify custom payload objects.
     */
    Class<? extends Payload>[] payload() default {};
}
