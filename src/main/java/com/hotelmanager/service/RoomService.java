package com.hotelmanager.service;

import com.hotelmanager.model.dto.request.RoomCreationDto;
import com.hotelmanager.model.dto.request.RoomUpdateDto;
import com.hotelmanager.model.dto.response.RoomPageResponseDto;
import com.hotelmanager.model.dto.response.RoomResponseDto;
import com.hotelmanager.model.entity.Room;
import com.hotelmanager.model.enums.RoomStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface RoomService {

    RoomResponseDto createRoom(RoomCreationDto creationDto);

    RoomResponseDto updateRoom(String id, RoomUpdateDto roomUpdateDto);

    RoomResponseDto getRoomById(String id);

    Page<RoomPageResponseDto> getAllRooms(Optional<UUID> roomType, Optional<RoomStatus> roomStatus, Pageable pageable);

    Room getRoomEntityById(String roomId);

    void deleteRoomById(String id);
}
