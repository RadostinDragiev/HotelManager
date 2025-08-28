package com.hotelmanager.service;

import com.hotelmanager.model.dto.RoomCreationDto;
import com.hotelmanager.model.dto.RoomResponseDto;

public interface RoomService {

    RoomResponseDto createRoom(RoomCreationDto creationDto);
}
