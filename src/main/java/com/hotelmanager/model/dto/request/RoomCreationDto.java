package com.hotelmanager.model.dto.request;

import com.hotelmanager.model.enums.BedType;
import com.hotelmanager.model.enums.RoomStatus;
import com.hotelmanager.model.enums.RoomType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

import static com.hotelmanager.validation.ValidationMessages.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomCreationDto {

    @NotNull(message = ROOM_NUMBER_REQUIRED)
    @Pattern(regexp = "^[1-9]\\d*$", message = ROOM_NUMBER_POSITIVE)
    @Size(max = 10, message = ROOM_NUMBER_MAX)
    private String roomNumber;

    @NotNull(message = ROOM_TYPE_REQUIRED)
    private RoomType roomType;

    @Positive(message = CAPACITY_POSITIVE)
    @Max(value = 9, message = CAPACITY_MAX)
    private int capacity;

    @NotNull(message = BED_TYPES_REQUIRED)
    @Size(min = 1, message = BED_TYPES_MORE_THAN_ONE)
    private List<BedType> bedTypes;

    @NotNull(message = PRICE_PER_NIGHT_REQUIRED)
    @DecimalMin(value = "0.0", inclusive = false, message = PRICE_PER_NIGHT_POSITIVE)
    @Digits(integer = 10, fraction = 2, message = PRICE_PER_NIGHT_MONETARY)
    private BigDecimal pricePerNight;

    @Size(max = 3000, message = DESCRIPTION_MAX_LENGTH)
    private String description;

    @NotNull(message = ROOM_STATUS_REQUIRED)
    private RoomStatus roomStatus;
}
