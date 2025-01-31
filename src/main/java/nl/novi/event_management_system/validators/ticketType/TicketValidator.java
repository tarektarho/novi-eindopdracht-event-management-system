package nl.novi.event_management_system.validators.ticketType;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import nl.novi.event_management_system.enums.TicketType;

import java.util.Set;

public class TicketValidator implements ConstraintValidator<ValidTicketType, TicketType> {
    @Override
    public boolean isValid(TicketType tickets, ConstraintValidatorContext context) {
        if(tickets == null) {
            return true;
        }

        try {
            TicketType.valueOf(tickets.name());
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
