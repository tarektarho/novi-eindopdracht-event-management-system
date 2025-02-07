package nl.novi.event_management_system.validators.ticketType;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import nl.novi.event_management_system.enums.TicketType;

public class TicketValidator implements ConstraintValidator<ValidTicketType, TicketType> {

    @Override
    public boolean isValid(TicketType ticketType, ConstraintValidatorContext context) {
        return ticketType != null && isValidEnum(ticketType);
    }

    private boolean isValidEnum(TicketType ticketType) {
        for (TicketType validType : TicketType.values()) {
            if (validType == ticketType) {
                return true;
            }
        }
        return false;
    }
}
