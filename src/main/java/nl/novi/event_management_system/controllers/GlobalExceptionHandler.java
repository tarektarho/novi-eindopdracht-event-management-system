package nl.novi.event_management_system.controllers;

import nl.novi.event_management_system.exceptions.RecordNotFoundException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RecordNotFoundException.class)
    public String handleNotFoundException(RecordNotFoundException recordNotFoundException) {
        return recordNotFoundException.getMessage();
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public String handleNotFoundException(InternalAuthenticationServiceException internalAuthenticationServiceException) {
        return internalAuthenticationServiceException.getMessage();
    }

}