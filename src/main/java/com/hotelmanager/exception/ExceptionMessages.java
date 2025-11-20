package com.hotelmanager.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionMessages {

    public static final String ROOM_NOT_FOUND_ID = "No room found by the provided id: ";

    public static final String NO_USER_FOUND_BY_USERNAME = "No user with provided username found!";
    public static final String NO_USER_FOUND_BY_ID = "No user found by the provided id!";

    public static final String OLD_PASSWORD_DOES_NOT_MATCH = "Provided old password is invalid!";
    public static final String NEW_PASSWORDS_DOES_NOT_MATCH = "New password and confirm new password should match!";
}
