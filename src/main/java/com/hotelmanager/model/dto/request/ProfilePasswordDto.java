package com.hotelmanager.model.dto.request;

import com.hotelmanager.validation.annotation.Password;
import lombok.*;

import static com.hotelmanager.validation.ValidationMessages.PASSWORD_SIZE;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfilePasswordDto {

    @Password(message = PASSWORD_SIZE)
    private String oldPassword;

    @Password(message = PASSWORD_SIZE)
    private String newPassword;

    @Password(message = PASSWORD_SIZE)
    private String confirmNewPassword;
}
