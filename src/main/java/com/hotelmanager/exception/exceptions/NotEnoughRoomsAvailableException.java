package com.hotelmanager.exception.exceptions;

public class NotEnoughRoomsAvailableException extends RuntimeException {
    public NotEnoughRoomsAvailableException(String message) {
        super(message);
    }
}
