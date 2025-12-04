package com.hotelmanager.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionMessages {

    public static final String USER_UNAUTHORIZED = "User is unauthorized to perform this action!";
    public static final String ROLE_NOT_FOUND = "Invalid role ids provided!";
    public static final String ROOM_TYPE_NOT_FOUND = "Room type not found!";
    public static final String ROOM_TYPE_EXISTS = "Room type already exists!";
    public static final String ROOM_NUMBER_EXISTS = "Room with number %s already exists!";

    public static final String ROOM_NOT_FOUND_ID = "No room found by the provided id: ";

    public static final String NO_USER_FOUND_BY_USERNAME = "No user with provided username found!";
    public static final String NO_USER_FOUND_BY_ID = "No user found by the provided id!";

    public static final String OLD_PASSWORD_DOES_NOT_MATCH = "Provided old password is invalid!";
    public static final String NEW_PASSWORDS_DOES_NOT_MATCH = "New password and confirm new password should match!";

    public static final String NOT_ENOUGH_ROOMS_AVAILABLE = "Not enough rooms of type %s available!";

    public static final String RESERVATION_NOT_FOUND = "No reservation found by the provided id!";

    public static final String PAYMENT_NOT_FOUND = "No payment found by the provided id!";
}
