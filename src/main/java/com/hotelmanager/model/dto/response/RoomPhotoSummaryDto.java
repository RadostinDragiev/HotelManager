package com.hotelmanager.model.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomPhotoSummaryDto {

    private String publicId;
    private String secureUrl;
    private LocalDateTime createdDateTime;
    private String createdBy;
}
