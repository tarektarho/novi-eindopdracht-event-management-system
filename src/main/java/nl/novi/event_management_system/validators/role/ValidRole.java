package nl.novi.event_management_system.validators.role;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RoleValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRole {
    String message() default "Invalid role. Each role must start with 'ROLE_ (e.g. ROLE_ADMIN)'";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
