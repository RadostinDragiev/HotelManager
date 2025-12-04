package com.hotelmanager.model.dto.response;

import com.hotelmanager.model.entity.RoomType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRoomDto {

    private UUID uuid;
    private String roomNumber;
    private RoomType roomType;
}
