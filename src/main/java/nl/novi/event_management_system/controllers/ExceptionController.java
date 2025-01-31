package nl.novi.event_management_system.controllers;

import nl.novi.event_management_system.exceptions.RecordNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestControllerAdvice
@CrossOrigin
@ControllerAdvice
public class ExceptionController {

    Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(value = RecordNotFoundException.class)
    public ResponseEntity<Void> exception(RecordNotFoundException exception) {

        logger.warn(exception.getMessage());
        return ResponseEntity.notFound().build();

    }

    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<String> exception(IOException exception){
        String message = "Problem with file storage" + exception.getMessage();
        logger.warn(message);
        return ResponseEntity.internalServerError().body(message);

    }

}