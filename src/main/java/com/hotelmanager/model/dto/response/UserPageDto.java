package com.hotelmanager.model.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPageDto {

    private String uuid;
    private String firstName;
    private String lastName;
    private String position;
    private boolean isEnabled;
    private LocalDateTime createdDateTime;
}
