package com.hotelmanager.model.dto.request;

import com.hotelmanager.model.enums.BedType;
import com.hotelmanager.model.enums.RoomStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

import static com.hotelmanager.validation.ValidationMessages.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomUpdateDto {

    @NotNull(message = ROOM_NUMBER_REQUIRED)
    @Pattern(regexp = "^[1-9]\\d*$", message = ROOM_NUMBER_POSITIVE)
    @Size(max = 10, message = ROOM_NUMBER_MAX)
    private String roomNumber;

    @NotNull(message = ROOM_TYPE_REQUIRED)
    private String roomType;

    @NotNull(message = BED_TYPES_REQUIRED)
    @Size(min = 1, message = BED_TYPES_MORE_THAN_ONE)
    private List<BedType> bedTypes;

    @NotNull(message = ROOM_STATUS_REQUIRED)
    private RoomStatus roomStatus;
}
