package com.hotelmanager.validation;

public final class ValidationMessages {

    public static final String ROOM_NUMBER_REQUIRED = "Room number is required";
    public static final String ROOM_NUMBER_POSITIVE = "Room number must be positive";
    public static final String ROOM_NUMBER_MAX = "Room number must not exceed 10 digits";

    public static final String ROOM_TYPE_REQUIRED = "Room type is required";
    public static final String ROOM_TYPE_NAME_NOT_NULL = "Room type name cannot be null!";
    public static final String ROOM_TYPE_NAME_SIZE = "Room type name should be between 5 and 50 characters!";

    public static final String CAPACITY_POSITIVE = "Capacity must be positive";
    public static final String CAPACITY_MAX = "Capacity must be less than 10";

    public static final String BED_TYPES_REQUIRED = "At least one bed type is required";
    public static final String BED_TYPES_MORE_THAN_ONE = "At least one bed type must be provided";

    public static final String PRICE_PER_NIGHT_REQUIRED = "Price per night is required";
    public static final String PRICE_PER_NIGHT_POSITIVE = "Price must be greater than 0";
    public static final String PRICE_PER_NIGHT_MONETARY = "Price must be a valid monetary amount";

    public static final String DESCRIPTION_MAX_LENGTH = "Description must not exceed 3000 characters";

    public static final String ROOM_STATUS_REQUIRED = "Room status is required";

    public static final String UNIQUE_USERNAME = "Username must be unique!";
    public static final String USERNAME_NOT_NULL = "Username should not be null!";
    public static final String USERNAME_SIZE = "Username should be between 4 and 20 characters!";
    public static final String PASSWORD_NOT_NULL = "Password should not be null!";
    public static final String PASSWORD_SIZE = "Password should be between 8 and 20 characters!";
    public static final String VALID_EMAIL = "Invalid email format!";
    public static final String EMAIL_NOT_NULL = "Email should not be null!";
    public static final String FIRST_NAME_NOT_NULL = "First name should not be null!";
    public static final String FIRST_NAME_SIZE = "First name should be between 2 and 50 characters!";
    public static final String LAST_NAME_NOT_NULL = "Last name should not be null!";
    public static final String LAST_NAME_SIZE = "Last name should be between 2 and 50 characters!";
    public static final String POSITION_NOT_NULL = "Position should not be null!";
    public static final String POSITION_SIZE = "Position should be between 2 and 50 characters!";
    public static final String IS_ENABLED_NOT_NULL = "Enabled should not be null!";
    public static final String ROLES_NOT_EMPTY = "Roles should not be empty!";

    public static final String PHONE_NOT_NULL = "Phone should not be null!";
    public static final String START_DATE_NOT_NULL = "Start date cannot be null!";
    public static final String START_DATE_NOT_PAST = "Start date cannot be in the past!";
    public static final String START_DATE_BEFORE_END_DATE = "Start date must be before end date!";
    public static final String END_DATE_NOT_NULL = "End date cannot be null!";
    public static final String END_DATE_NOT_PAST = "End date cannot be in the past!";
    public static final String ROOMS_LIST_NOT_EMPTY = "Rooms list cannot be empty!";
    public static final String GUESTS_COUNT_ONLY_POSITIVE = "Guests count cannot be null!";

    public static final String ROOMS_COUNT_ONLY_POSITIVE = "Rooms count must be positive!";
    public static final String ROOM_TYPE_NOT_NULL = "Room type cannot be null!";

    private ValidationMessages() {
    }
}
