package com.hotelmanager.service;

import com.hotelmanager.model.dto.request.ProfilePasswordDto;
import com.hotelmanager.model.dto.request.UserDto;
import com.hotelmanager.model.dto.response.ProfileDto;
import com.hotelmanager.model.dto.response.UserDetailsDto;
import com.hotelmanager.model.dto.response.UserPageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {

    void updateLastLogin(String username);

    UUID createUser(UserDto userDto);

    ProfileDto getUserProfile();

    Page<UserPageDto> getAllUsers(Pageable pageable);

    UserDetailsDto getUserById(String id);

    void updateProfilePassword(ProfilePasswordDto passwordDto);

    void activateUser(String id);

    void deactivateUser(String id);
}
