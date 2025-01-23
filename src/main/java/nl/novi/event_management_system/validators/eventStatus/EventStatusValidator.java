package nl.novi.event_management_system.validators.eventStatus;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import nl.novi.event_management_system.enums.EventStatus;

public class EventStatusValidator implements ConstraintValidator<ValidEventStatus, EventStatus> {

    @Override
    public boolean isValid(EventStatus status, ConstraintValidatorContext context) {
        if (status == null) {
            return false;
        }

        try {
            EventStatus.valueOf(status.name());  // Check if it's a valid enum value
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
