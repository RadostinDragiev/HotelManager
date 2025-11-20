package com.hotelmanager.model.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String position;
}
