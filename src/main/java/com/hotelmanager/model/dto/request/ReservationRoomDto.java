package com.hotelmanager.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import static com.hotelmanager.validation.ValidationMessages.ROOMS_COUNT_ONLY_POSITIVE;
import static com.hotelmanager.validation.ValidationMessages.ROOM_TYPE_NOT_NULL;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRoomDto {

    @Positive(message = ROOMS_COUNT_ONLY_POSITIVE)
    private int roomsCount;

    @NotNull(message = ROOM_TYPE_NOT_NULL)
    private String roomTypeName;

    private String roomId;
}
