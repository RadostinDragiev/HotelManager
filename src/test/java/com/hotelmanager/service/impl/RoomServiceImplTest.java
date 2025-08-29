package com.hotelmanager.service.impl;

import com.hotelmanager.model.dto.request.RoomCreationDto;
import com.hotelmanager.model.dto.request.RoomUpdateDto;
import com.hotelmanager.model.dto.response.RoomResponseDto;
import com.hotelmanager.model.entity.Room;
import com.hotelmanager.model.enums.BedType;
import com.hotelmanager.model.enums.RoomStatus;
import com.hotelmanager.model.enums.RoomType;
import com.hotelmanager.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RoomServiceImpl roomService;

    @Test
    void createRoom_shouldReturnCreatedRoom() {
        RoomCreationDto creationDto = new RoomCreationDto();
        creationDto.setRoomNumber("102");
        creationDto.setRoomType(RoomType.DOUBLE);
        creationDto.setCapacity(3);
        creationDto.setBedTypes(List.of(BedType.DOUBLE));
        creationDto.setPricePerNight(BigDecimal.valueOf(200));
        creationDto.setDescription("New room");
        creationDto.setRoomStatus(RoomStatus.AVAILABLE);

        Room newRoom = new Room();
        when(modelMapper.map(creationDto, Room.class)).thenReturn(newRoom);
        when(roomRepository.save(newRoom)).thenReturn(newRoom);

        RoomResponseDto responseDto = new RoomResponseDto();
        when(modelMapper.map(newRoom, RoomResponseDto.class)).thenReturn(responseDto);

        RoomResponseDto result = roomService.createRoom(creationDto);

        assertThat(result).isNotNull();
        verify(modelMapper, times(1)).map(creationDto, Room.class);
        verify(roomRepository, times(1)).save(newRoom);
        verify(modelMapper, times(1)).map(newRoom, RoomResponseDto.class);
    }

    @Test
    void updateRoom_shouldThrowEntityNotFound_whenRoomDoesNotExist() {
        String nonExistingId = UUID.randomUUID().toString();
        RoomUpdateDto updateDto = new RoomUpdateDto();

        when(this.roomRepository.findById(UUID.fromString(nonExistingId))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.roomService.updateRoom(nonExistingId, updateDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Invalid room id " + nonExistingId);

        verify(this.roomRepository, times(1)).findById(UUID.fromString(nonExistingId));
        verifyNoMoreInteractions(this.roomRepository);
    }

    @Test
    void updateRoom_shouldReturnUpdatedRoom_whenRoomExists() {
        String existingId = UUID.randomUUID().toString();
        RoomUpdateDto updateDto = new RoomUpdateDto();
        updateDto.setRoomNumber("101");
        updateDto.setRoomType(RoomType.SINGLE);
        updateDto.setCapacity(2);
        updateDto.setBedTypes(List.of(BedType.SINGLE));
        updateDto.setPricePerNight(BigDecimal.valueOf(150));
        updateDto.setDescription("Updated room");
        updateDto.setRoomStatus(RoomStatus.AVAILABLE);

        Room existingRoom = new Room();
        when(roomRepository.findById(UUID.fromString(existingId)))
                .thenReturn(Optional.of(existingRoom));
        when(roomRepository.save(existingRoom)).thenReturn(existingRoom);

        RoomResponseDto responseDto = new RoomResponseDto();
        when(modelMapper.map(existingRoom, RoomResponseDto.class))
                .thenReturn(responseDto);

        RoomResponseDto result = roomService.updateRoom(existingId, updateDto);

        assertThat(result).isNotNull();
        verify(roomRepository, times(1))
                .findById(UUID.fromString(existingId));
        verify(roomRepository, times(1)).save(existingRoom);
        verify(modelMapper, times(1))
                .map(existingRoom, RoomResponseDto.class);
    }
}