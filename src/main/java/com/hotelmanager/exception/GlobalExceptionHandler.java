package com.hotelmanager.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        log.error("Entity not found", ex);

        ExceptionErrorResponse response = new ExceptionErrorResponse();
        response.setMessage(ex.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.NOT_FOUND);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationConstraintErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Validation error", ex);

        ValidationConstraintErrorResponse response = new ValidationConstraintErrorResponse(HttpStatus.BAD_REQUEST);
        response.setMessage("Validation error");

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> response.addFieldError(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error("Database constraint violation: ", ex);

        ExceptionErrorResponse response = new ExceptionErrorResponse();
        response.setStatus(HttpStatus.CONFLICT);
        response.setTimestamp(LocalDateTime.now());
        response.setMessage("Database constraint violation");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionErrorResponse> handleAllExceptions(Exception ex) {
        log.error("Unhandled exception caught: ", ex);

        ExceptionErrorResponse response = new ExceptionErrorResponse();
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        response.setTimestamp(LocalDateTime.now());
        response.setMessage("Unexpected error occurred");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
