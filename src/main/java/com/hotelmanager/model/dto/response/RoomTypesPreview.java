package com.hotelmanager.model.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypesPreview {

    private String name;
    private int capacity;
    private BigDecimal basePricePerNight;
    private String description;
}
