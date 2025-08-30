package com.hotelmanager.service.impl;

import com.hotelmanager.exception.exceptions.RoomNotFoundException;
import com.hotelmanager.model.dto.request.RoomCreationDto;
import com.hotelmanager.model.dto.request.RoomUpdateDto;
import com.hotelmanager.model.dto.response.RoomPageResponseDto;
import com.hotelmanager.model.dto.response.RoomResponseDto;
import com.hotelmanager.model.entity.Room;
import com.hotelmanager.model.enums.RoomStatus;
import com.hotelmanager.model.enums.RoomType;
import com.hotelmanager.repository.RoomRepository;
import com.hotelmanager.service.RoomService;
import com.hotelmanager.validation.PageableValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.hotelmanager.exception.ExceptionMessages.ROOM_NOT_FOUND_ID;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;

    @Override
    public RoomResponseDto createRoom(RoomCreationDto creationDto) {
        Room newRoom = this.modelMapper.map(creationDto, Room.class);

        Room createdRoom = this.roomRepository.save(newRoom);
        return this.modelMapper.map(createdRoom, RoomResponseDto.class);
    }

    @Override
    public RoomResponseDto updateRoom(String id, RoomUpdateDto roomUpdateDto) {
        Room existingRoom = this.roomRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RoomNotFoundException(ROOM_NOT_FOUND_ID + id));

        existingRoom.setRoomNumber(roomUpdateDto.getRoomNumber());
        existingRoom.setRoomType(roomUpdateDto.getRoomType());
        existingRoom.setCapacity(roomUpdateDto.getCapacity());
        existingRoom.setBedTypes(roomUpdateDto.getBedTypes());
        existingRoom.setPricePerNight(roomUpdateDto.getPricePerNight());
        existingRoom.setDescription(roomUpdateDto.getDescription());
        existingRoom.setRoomStatus(roomUpdateDto.getRoomStatus());

        Room updatedRoom = this.roomRepository.save(existingRoom);
        return this.modelMapper.map(updatedRoom, RoomResponseDto.class);
    }

    @Override
    public RoomResponseDto getRoomById(String id) {
        Room room = this.roomRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RoomNotFoundException(ROOM_NOT_FOUND_ID + id));
        return this.modelMapper.map(room, RoomResponseDto.class);
    }

    @Override
    public Page<RoomPageResponseDto> getAllRooms(Optional<RoomType> roomType, Optional<RoomStatus> roomStatus, Pageable pageable) {
        Page<Room> rooms;
        if (roomType.isPresent() && roomStatus.isPresent()) {
            rooms = roomRepository.findByRoomTypeAndRoomStatus(roomType.get(), roomStatus.get(), pageable);
        } else if (roomType.isPresent()) {
            rooms = roomRepository.findByRoomType(roomType.get(), pageable);
        } else if (roomStatus.isPresent()) {
            rooms = roomRepository.findByRoomStatus(roomStatus.get(), pageable);
        } else {
            rooms = roomRepository.findAll(pageable);
        }

        PageableValidator.validatePageRequest(rooms, pageable);

        return rooms.map(r -> modelMapper.map(r, RoomPageResponseDto.class));
    }

    @Override
    public void deleteRoomById(String id) {
        if (!this.roomRepository.existsById(UUID.fromString(id))) {
            throw new RoomNotFoundException(ROOM_NOT_FOUND_ID + id);
        }
        this.roomRepository.deleteById(UUID.fromString(id));
    }
}
