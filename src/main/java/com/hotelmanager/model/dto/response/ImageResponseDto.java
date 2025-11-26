package com.hotelmanager.model.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageResponseDto {

    private UUID uuid;
    private String roomTypeId;
    private String publicId;
    private String secureUrl;
}
