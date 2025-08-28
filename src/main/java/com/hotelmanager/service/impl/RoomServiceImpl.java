package com.hotelmanager.service.impl;

import com.hotelmanager.model.dto.RoomCreationDto;
import com.hotelmanager.model.dto.RoomResponseDto;
import com.hotelmanager.model.entity.Room;
import com.hotelmanager.repository.RoomRepository;
import com.hotelmanager.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
}
