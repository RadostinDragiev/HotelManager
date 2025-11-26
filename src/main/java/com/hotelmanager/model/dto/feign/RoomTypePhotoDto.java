package com.hotelmanager.model.dto.feign;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypePhotoDto {

    private UUID uuid;
    private String roomTypeId;
    private String publicId;
    private String secureUrl;
    private LocalDateTime createdDateTime;
    private String createdBy;
}
