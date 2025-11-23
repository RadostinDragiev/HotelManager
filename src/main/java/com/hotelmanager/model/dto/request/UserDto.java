package com.hotelmanager.model.dto.request;

import com.hotelmanager.validation.annotation.UniqueUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;
import java.util.UUID;

import static com.hotelmanager.validation.ValidationMessages.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @UniqueUsername(message = UNIQUE_USERNAME)
    @NotNull(message = USERNAME_NOT_NULL)
    @Size(min = 4, max = 20, message = USERNAME_SIZE)
    private String username;

    @NotNull(message = PASSWORD_NOT_NULL)
    @Size(min = 8, max = 20, message = PASSWORD_SIZE)
    private String password;

    @NotNull(message = EMAIL_NOT_NULL)
    @Email(message = VALID_EMAIL)
    private String email;

    @NotNull(message = FIRST_NAME_NOT_NULL)
    @Size(min = 2, max = 50, message = FIRST_NAME_SIZE)
    private String firstName;

    @NotNull(message = LAST_NAME_NOT_NULL)
    @Size(min = 2, max = 50, message = LAST_NAME_SIZE)
    private String lastName;

    @NotNull(message = POSITION_NOT_NULL)
    @Size(min = 2, max = 50, message = POSITION_SIZE)
    private String position;

    @NotNull(message = IS_ENABLED_NOT_NULL)
    private Boolean isEnabled;

    @NotEmpty(message = ROLES_NOT_EMPTY)
    private Set<UUID> roles;
}
