package com.hotelmanager.exception.exceptions;

public class RoomTypeNotFoundException extends RuntimeException {
    public RoomTypeNotFoundException(String message) {
        super(message);
    }
}
