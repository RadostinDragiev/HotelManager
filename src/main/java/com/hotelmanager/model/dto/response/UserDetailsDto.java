package com.hotelmanager.model.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDto {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Set<RoleDto> roles;
    private LocalDateTime createdDateTime;
    private LocalDateTime lastLoginDateTime;
}
