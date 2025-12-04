package com.hotelmanager.exception.exceptions;

public class InvalidReservationPaymentTypeException extends RuntimeException {
    public InvalidReservationPaymentTypeException(String message) {
        super(message);
    }
}
