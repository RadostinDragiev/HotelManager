package com.hotelmanager.service;

import com.hotelmanager.model.dto.request.ProfilePasswordDto;
import com.hotelmanager.model.dto.request.UserDto;
import com.hotelmanager.model.dto.response.ProfileDto;

import java.util.UUID;

public interface UserService {

    void updateLastLogin(String username);

    UUID createUser(UserDto userDto);

    ProfileDto getUserProfile();

    void updateProfilePassword(ProfilePasswordDto passwordDto);

    void activateUser(String id);

    void deactivateUser(String id);
}
