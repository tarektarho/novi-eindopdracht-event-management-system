package nl.novi.event_management_system.validators.eventStatus;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EventStatusValidator.class)
public @interface ValidEventStatus {
    String message() default "Invalid event status.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
