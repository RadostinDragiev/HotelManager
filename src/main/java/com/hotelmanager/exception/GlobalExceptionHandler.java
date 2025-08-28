package com.hotelmanager.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles EntityNotFoundException. Created to encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
     *
     * @param ex the EntityNotFoundException
     * @return the ApiError object
     */
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<?> handleEntityNotFound(EntityNotFoundException ex) {
        ExceptionDetails apiError = new ExceptionDetails();
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDetails> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ExceptionDetails response = new ExceptionDetails(HttpStatus.BAD_REQUEST);
        response.setMessage("Validation error");

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> response.addFieldError(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    private ResponseEntity<?> buildResponseEntity(ExceptionDetails apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
