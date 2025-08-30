package com.hotelmanager.model.dto.response;

import com.hotelmanager.model.enums.BedType;
import com.hotelmanager.model.enums.RoomStatus;
import com.hotelmanager.model.enums.RoomType;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomPageResponseDto {

    private String uuid;
    private String roomNumber;
    private RoomType roomType;
    private int capacity;
    private List<BedType> bedTypes;
    private BigDecimal pricePerNight;
    private String description;
    private RoomStatus roomStatus;
}
