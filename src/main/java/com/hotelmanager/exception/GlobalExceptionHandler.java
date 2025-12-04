package com.hotelmanager.exception;

import com.hotelmanager.exception.exceptions.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static com.hotelmanager.exception.ExceptionMessages.USER_UNAUTHORIZED;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ExceptionErrorResponse> handlePaymentNotFoundException(PaymentNotFoundException ex) {
        log.error("Payment not found!", ex);

        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ExceptionErrorResponse> handleReservationNotFoundException(ReservationNotFoundException ex) {
        log.error("Reservation not found! ", ex);

        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(NotEnoughRoomsAvailableException.class)
    public ResponseEntity<ExceptionErrorResponse> handleNotEnoughRoomsAvailableException(NotEnoughRoomsAvailableException ex) {
        log.error("Not enough rooms available! ", ex);

        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(RoomTypeAlreadyExistsException.class)
    public ResponseEntity<ExceptionErrorResponse> handleRoomTypeAlreadyExistsException(RoomTypeAlreadyExistsException ex) {
        log.error("Room type already exists! ", ex);

        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(RoomNumberAlreadyExistsException.class)
    public ResponseEntity<ExceptionErrorResponse> handleRoomNumberAlreadyExistsException(RoomNumberAlreadyExistsException ex) {
        log.error("Room with provided number already exists! ", ex);

        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(RoomTypeNotFoundException.class)
    public ResponseEntity<ExceptionErrorResponse> handleRoomTypeNotFoundException(RoomTypeNotFoundException ex) {
        log.error("Room type not found! ", ex);

        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(PasswordsDoesNotMatchException.class)
    public ResponseEntity<ExceptionErrorResponse> handleUserNotFoundException(PasswordsDoesNotMatchException ex) {
        log.error("Passwords does not match! ", ex);

        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        log.error("User not found! ", ex);

        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(RolesNotFoundException.class)
    public ResponseEntity<ExceptionErrorResponse> handleRolesNotFoundException(RolesNotFoundException ex) {
        log.error("Roles not found! ", ex);

        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        log.error("Bad credentials!", ex);

        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionErrorResponse> handleLockedException(LockedException ex) {
        log.error("User is locked!", ex);

        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionErrorResponse> handleLockedException(DisabledException ex) {
        log.error("User is disabled!", ex);

        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        log.error("No user found with the provided credentials!", ex);

        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ExceptionErrorResponse> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        log.error("Access denied!", ex);

        return buildResponse(HttpStatus.UNAUTHORIZED, USER_UNAUTHORIZED);
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

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        log.error("Entity not found", ex);

        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error("Database constraint violation: ", ex);

        return buildResponse(HttpStatus.CONFLICT, "Database constraint violation!");
    }

    @ExceptionHandler(PageOutOfBoundsException.class)
    public ResponseEntity<ExceptionErrorResponse> handlePageOutOfBoundsException(PageOutOfBoundsException ex) {
        log.error("Page out of bounds! ", ex);

        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<ExceptionErrorResponse> handleRoomNotFoundException(RoomNotFoundException ex) {
        log.error("Room with provided id not found! ", ex);

        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionErrorResponse> handleAllExceptions(Exception ex) {
        log.error("Unhandled exception caught: ", ex);

        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
    }

    private static ResponseEntity<ExceptionErrorResponse> buildResponse(HttpStatus status, String message) {
        ExceptionErrorResponse response = ExceptionErrorResponse.builder()
                .status(status)
                .timestamp(LocalDateTime.now())
                .message(message)
                .build();
        return ResponseEntity.status(status).body(response);
    }
}
