package com.hotelmanager.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hotelmanager.model.enums.RoomStatus;
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
    private String roomType;
    private int capacity;
    private List<String> bedTypes;
    private BigDecimal pricePerNight;
    private String description;
    private RoomStatus roomStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    private List<RoomPhotoSummaryDto> photos;
    private List<RoomPhotoSummaryDto> roomTypePhotos;
}
