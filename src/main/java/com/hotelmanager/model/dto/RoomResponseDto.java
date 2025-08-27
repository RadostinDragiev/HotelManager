package com.hotelmanager.model.dto;

import com.hotelmanager.model.enums.BedType;
import com.hotelmanager.model.enums.RoomStatus;
import com.hotelmanager.model.enums.RoomType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponseDto {

    private UUID uuid;
    private String roomNumber;
    private RoomType roomType;
    private int capacity;
    private List<BedType> bedTypes;
    private BigDecimal pricePerNight;
    private String description;
    private RoomStatus roomStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
