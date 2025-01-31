package nl.novi.event_management_system.validators.ticketType;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = TicketValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTicketType {
    String message() default "Invalid ticket type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
