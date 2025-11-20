package com.hotelmanager.exception.exceptions;

public class PasswordsDoesNotMatchException extends RuntimeException {
    public PasswordsDoesNotMatchException(String message) {
        super(message);
    }
}
