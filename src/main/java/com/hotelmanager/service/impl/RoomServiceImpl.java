package com.hotelmanager.service.impl;

import com.hotelmanager.model.dto.request.RoomCreationDto;
import com.hotelmanager.model.dto.request.RoomUpdateDto;
import com.hotelmanager.model.dto.response.RoomResponseDto;
import com.hotelmanager.model.entity.Room;
import com.hotelmanager.repository.RoomRepository;
import com.hotelmanager.service.RoomService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
                .orElseThrow(() -> new EntityNotFoundException("Invalid room id " + id));

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
}
