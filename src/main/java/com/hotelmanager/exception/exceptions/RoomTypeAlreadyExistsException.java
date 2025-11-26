package com.hotelmanager.exception.exceptions;

public class RoomTypeAlreadyExistsException extends RuntimeException {
    public RoomTypeAlreadyExistsException(String message) {
        super(message);
    }
}
