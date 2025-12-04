package com.hotelmanager.model.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRoomTypeDto {

    private String roomTypeName;
    private int roomsCount;
}
