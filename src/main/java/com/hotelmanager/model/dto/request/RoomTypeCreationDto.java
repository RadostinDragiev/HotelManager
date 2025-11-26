package com.hotelmanager.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

import static com.hotelmanager.validation.ValidationMessages.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypeCreationDto {

    @NotNull(message = ROOM_TYPE_NAME_NOT_NULL)
    @Size(min = 5, max = 50, message = ROOM_TYPE_NAME_SIZE)
    private String name;

    @NotNull(message = PRICE_PER_NIGHT_REQUIRED)
    @DecimalMin(value = "0.0", inclusive = false, message = PRICE_PER_NIGHT_POSITIVE)
    @Digits(integer = 10, fraction = 2, message = PRICE_PER_NIGHT_MONETARY)
    private BigDecimal basePricePerNight;

    @Positive(message = CAPACITY_POSITIVE)
    @Max(value = 9, message = CAPACITY_MAX)
    private int capacity;

    @Size(max = 3000, message = DESCRIPTION_MAX_LENGTH)
    private String description;
}
