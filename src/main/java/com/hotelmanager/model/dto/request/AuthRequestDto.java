package com.hotelmanager.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestDto {

    @NotNull(message = "Username should not be empty!")
    @Size(min = 4, max = 20, message = "Username should be between 4 and 20 characters!")
    private String username;

    @NotNull(message = "Password should not be empty!")
    @Size(min = 8, max = 20, message = "Password should be between 8 and 20 characters!")
    private String password;
}
