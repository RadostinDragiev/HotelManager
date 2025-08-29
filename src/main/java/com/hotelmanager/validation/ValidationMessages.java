package com.hotelmanager.validation;

public final class ValidationMessages {

    public static final String ROOM_NUMBER_REQUIRED = "Room number is required";
    public static final String ROOM_NUMBER_POSITIVE = "Room number must be positive";
    public static final String ROOM_NUMBER_MAX = "Room number must not exceed 10 digits";

    public static final String ROOM_TYPE_REQUIRED = "Room type is required";

    public static final String CAPACITY_POSITIVE = "Capacity must be positive";
    public static final String CAPACITY_MAX = "Capacity must be less than 10";

    public static final String BED_TYPES_REQUIRED = "At least one bed type is required";
    public static final String BED_TYPES_MORE_THAN_ONE = "At least one bed type must be provided";

    public static final String PRICE_PER_NIGHT_REQUIRED = "Price per night is required";
    public static final String PRICE_PER_NIGHT_POSITIVE = "Price must be greater than 0";
    public static final String PRICE_PER_NIGHT_MONETARY = "Price must be a valid monetary amount";

    public static final String DESCRIPTION_MAX_LENGTH = "Description must not exceed 3000 characters";

    public static final String ROOM_STATUS_REQUIRED = "Room status is required";

    private ValidationMessages() {
    }
}
