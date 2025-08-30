package com.hotelmanager.service.impl;

import com.hotelmanager.exception.exceptions.RoomNotFoundException;
import com.hotelmanager.model.dto.request.RoomCreationDto;
import com.hotelmanager.model.dto.request.RoomUpdateDto;
import com.hotelmanager.model.dto.response.RoomPageResponseDto;
import com.hotelmanager.model.dto.response.RoomResponseDto;
import com.hotelmanager.model.entity.Room;
import com.hotelmanager.model.enums.BedType;
import com.hotelmanager.model.enums.RoomStatus;
import com.hotelmanager.model.enums.RoomType;
import com.hotelmanager.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.hotelmanager.exception.ExceptionMessages.ROOM_NOT_FOUND_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    private static final UUID RANDOM = UUID.randomUUID();

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RoomServiceImpl roomService;

    private Room room;
    private RoomResponseDto roomResponseDto;

    @BeforeEach
    void setUp() {
        this.room = new Room();
        this.room.setRoomNumber("101");
        this.room.setRoomType(RoomType.SINGLE);
        this.room.setRoomStatus(RoomStatus.AVAILABLE);

        this.roomResponseDto = new RoomResponseDto();
        this.roomResponseDto.setUuid(RANDOM);
        this.roomResponseDto.setRoomNumber("101");
    }


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
        when(this.modelMapper.map(creationDto, Room.class)).thenReturn(newRoom);
        when(this.roomRepository.save(newRoom)).thenReturn(newRoom);

        RoomResponseDto responseDto = new RoomResponseDto();
        when(this.modelMapper.map(newRoom, RoomResponseDto.class)).thenReturn(responseDto);

        RoomResponseDto result = this.roomService.createRoom(creationDto);

        assertThat(result).isNotNull();
        verify(this.modelMapper, times(1)).map(creationDto, Room.class);
        verify(this.roomRepository, times(1)).save(newRoom);
        verify(this.modelMapper, times(1)).map(newRoom, RoomResponseDto.class);
    }

    @Test
    void updateRoom_shouldThrowRoomNotFound_whenRoomDoesNotExist() {
        String nonExistingId = UUID.randomUUID().toString();
        RoomUpdateDto updateDto = new RoomUpdateDto();

        when(this.roomRepository.findById(UUID.fromString(nonExistingId))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.roomService.updateRoom(nonExistingId, updateDto))
                .isInstanceOf(RoomNotFoundException.class)
                .hasMessageContaining(ROOM_NOT_FOUND_ID + nonExistingId);

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
        when(this.roomRepository.findById(UUID.fromString(existingId)))
                .thenReturn(Optional.of(existingRoom));
        when(this.roomRepository.save(existingRoom)).thenReturn(existingRoom);

        RoomResponseDto responseDto = new RoomResponseDto();
        when(this.modelMapper.map(existingRoom, RoomResponseDto.class))
                .thenReturn(responseDto);

        RoomResponseDto result = this.roomService.updateRoom(existingId, updateDto);

        assertThat(result).isNotNull();
        verify(this.roomRepository, times(1))
                .findById(UUID.fromString(existingId));
        verify(this.roomRepository, times(1)).save(existingRoom);
        verify(this.modelMapper, times(1))
                .map(existingRoom, RoomResponseDto.class);
    }

    @Test
    void getRoomById_shouldReturnRoomResponse_whenRoomExists() {
        when(this.roomRepository.findById(RANDOM)).thenReturn(Optional.of(room));
        when(this.modelMapper.map(room, RoomResponseDto.class)).thenReturn(roomResponseDto);

        RoomResponseDto result = this.roomService.getRoomById(RANDOM.toString());

        assertThat(result.getUuid()).isEqualTo(RANDOM);
        assertThat(result.getRoomNumber()).isEqualTo("101");
        verify(this.roomRepository).findById(RANDOM);
        verify(this.modelMapper).map(room, RoomResponseDto.class);
    }

    @Test
    void getRoomById_shouldThrowRoomNotFound_whenRoomDoesNotExist() {
        UUID fakeId = UUID.randomUUID();
        when(this.roomRepository.findById(fakeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.roomService.getRoomById(fakeId.toString()))
                .isInstanceOf(RoomNotFoundException.class)
                .hasMessageContaining(ROOM_NOT_FOUND_ID + fakeId);
    }

    @Test
    void getAllRooms_shouldReturnAllRooms_whenNoFiltersProvided() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("roomNumber"));
        Page<Room> rooms = new PageImpl<>(List.of(room));

        when(this.roomRepository.findAll(pageable)).thenReturn(rooms);
        when(this.modelMapper.map(room, RoomPageResponseDto.class)).thenReturn(new RoomPageResponseDto());

        Page<RoomPageResponseDto> result = this.roomService.getAllRooms(Optional.empty(), Optional.empty(), pageable);

        assertThat(result).hasSize(1);
        verify(this.roomRepository).findAll(pageable);
    }

    @Test
    void getAllRooms_shouldFilterByRoomType_whenOnlyRoomTypeProvided() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Room> rooms = new PageImpl<>(List.of(room));

        when(this.roomRepository.findByRoomType(RoomType.SINGLE, pageable)).thenReturn(rooms);
        when(this.modelMapper.map(room, RoomPageResponseDto.class)).thenReturn(new RoomPageResponseDto());

        Page<RoomPageResponseDto> result = this.roomService.getAllRooms(Optional.of(RoomType.SINGLE), Optional.empty(), pageable);

        assertThat(result).hasSize(1);
        verify(this.roomRepository).findByRoomType(RoomType.SINGLE, pageable);
    }

    @Test
    void getAllRooms_shouldFilterByRoomStatus_whenOnlyRoomStatusProvided() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Room> rooms = new PageImpl<>(List.of(room));

        when(this.roomRepository.findByRoomStatus(RoomStatus.AVAILABLE, pageable)).thenReturn(rooms);
        when(this.modelMapper.map(room, RoomPageResponseDto.class)).thenReturn(new RoomPageResponseDto());

        Page<RoomPageResponseDto> result = this.roomService.getAllRooms(Optional.empty(), Optional.of(RoomStatus.AVAILABLE), pageable);

        assertThat(result).hasSize(1);
        verify(this.roomRepository).findByRoomStatus(RoomStatus.AVAILABLE, pageable);
    }

    @Test
    void getAllRooms_shouldFilterByRoomTypeAndStatus_whenBothProvided() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Room> rooms = new PageImpl<>(List.of(room));

        when(this.roomRepository.findByRoomTypeAndRoomStatus(RoomType.SINGLE, RoomStatus.AVAILABLE, pageable)).thenReturn(rooms);
        when(this.modelMapper.map(room, RoomPageResponseDto.class)).thenReturn(new RoomPageResponseDto());

        Page<RoomPageResponseDto> result = this.roomService.getAllRooms(Optional.of(RoomType.SINGLE), Optional.of(RoomStatus.AVAILABLE), pageable);

        assertThat(result).hasSize(1);
        verify(this.roomRepository).findByRoomTypeAndRoomStatus(RoomType.SINGLE, RoomStatus.AVAILABLE, pageable);
    }

    @Test
    void deleteRoomById_shouldThrowRoomNotFound_whenRoomDoesNotExist() {
        UUID fakeId = UUID.randomUUID();
        when(this.roomRepository.existsById(fakeId)).thenReturn(false);

        assertThatThrownBy(() -> this.roomService.deleteRoomById(fakeId.toString()))
                .isInstanceOf(RoomNotFoundException.class)
                .hasMessageContaining(ROOM_NOT_FOUND_ID + fakeId);
        verify(roomRepository, never()).deleteById(any());
    }

    @Test
    void deleteRoomById_shouldReturnRoomResponse_whenRoomExists() {
        UUID roomId = UUID.randomUUID();
        when(this.roomRepository.existsById(roomId)).thenReturn(true);

        this.roomService.deleteRoomById(roomId.toString());

        verify(roomRepository, times(1)).deleteById(UUID.fromString(roomId.toString()));
    }
}