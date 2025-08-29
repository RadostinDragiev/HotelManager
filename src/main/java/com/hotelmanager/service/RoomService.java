package com.hotelmanager.service;

import com.hotelmanager.model.dto.request.RoomCreationDto;
import com.hotelmanager.model.dto.request.RoomUpdateDto;
import com.hotelmanager.model.dto.response.RoomResponseDto;

public interface RoomService {

    RoomResponseDto createRoom(RoomCreationDto creationDto);

    RoomResponseDto updateRoom(String id, RoomUpdateDto roomUpdateDto);
}
