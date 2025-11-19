package com.hotelmanager.service;

import com.hotelmanager.model.dto.request.UserDto;

import java.util.UUID;

public interface UserService {

    void updateLastLogin(String username);

    UUID createUser(UserDto userDto);

    void deactivateUser(String id);
}
