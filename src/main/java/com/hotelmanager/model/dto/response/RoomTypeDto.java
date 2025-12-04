package com.hotelmanager.model.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypeDto {

    private UUID uuid;
    private String name;
    private BigDecimal basePricePerNight;
    private int capacity;
    private String description;
    private List<ImageResponseDto> images;
}
