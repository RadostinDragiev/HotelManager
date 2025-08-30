package com.hotelmanager.service;

import com.hotelmanager.model.dto.request.RoomCreationDto;
import com.hotelmanager.model.dto.request.RoomUpdateDto;
import com.hotelmanager.model.dto.response.RoomPageResponseDto;
import com.hotelmanager.model.dto.response.RoomResponseDto;
import com.hotelmanager.model.enums.RoomStatus;
import com.hotelmanager.model.enums.RoomType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RoomService {

    RoomResponseDto createRoom(RoomCreationDto creationDto);

    RoomResponseDto updateRoom(String id, RoomUpdateDto roomUpdateDto);

    RoomResponseDto getRoomById(String id);

    Page<RoomPageResponseDto> getAllRooms(Optional<RoomType> roomType, Optional<RoomStatus> status, Pageable pageable);
}
