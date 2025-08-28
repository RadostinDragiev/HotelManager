package com.hotelmanager.model.dto;

import com.hotelmanager.model.enums.BedType;
import com.hotelmanager.model.enums.RoomStatus;
import com.hotelmanager.model.enums.RoomType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomCreationDto {

    @NotNull(message = "Room number is required")
    @Pattern(regexp = "^[1-9][0-9]*$", message = "Room number must be positive")
    @Size(max = 10, message = "Room number must not exceed 10 digits")
    private String roomNumber;

    @NotNull(message = "Room type is required")
    private RoomType roomType;

    @Positive(message = "Capacity must be positive")
    @Max(value = 9, message = "Capacity must be less than 10")
    private int capacity;

    @NotNull(message = "At least one bed type is required")
    @Size(min = 1, message = "At least one bed type must be provided")
    private List<BedType> bedTypes;

    @NotNull(message = "Price per night is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Price must be a valid monetary amount")
    private BigDecimal pricePerNight;

    @Size(max = 3000, message = "Description must not exceed 3000 characters")
    private String description;

    @NotNull(message = "Room status is required")
    private RoomStatus roomStatus;
}
