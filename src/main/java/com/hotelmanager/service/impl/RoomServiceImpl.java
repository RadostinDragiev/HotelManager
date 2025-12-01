package com.hotelmanager.service.impl;

import com.hotelmanager.exception.exceptions.RoomNotFoundException;
import com.hotelmanager.exception.exceptions.RoomNumberAlreadyExistsException;
import com.hotelmanager.model.dto.request.RoomCreationDto;
import com.hotelmanager.model.dto.request.RoomUpdateDto;
import com.hotelmanager.model.dto.response.RoomPageResponseDto;
import com.hotelmanager.model.dto.response.RoomPhotoSummaryDto;
import com.hotelmanager.model.dto.response.RoomResponseDto;
import com.hotelmanager.model.entity.Room;
import com.hotelmanager.model.entity.RoomType;
import com.hotelmanager.model.entity.User;
import com.hotelmanager.model.enums.BedType;
import com.hotelmanager.model.enums.RoomStatus;
import com.hotelmanager.repository.RoomRepository;
import com.hotelmanager.service.FilesService;
import com.hotelmanager.service.RoomService;
import com.hotelmanager.service.RoomTypeService;
import com.hotelmanager.service.UserService;
import com.hotelmanager.specifications.RoomSpecifications;
import com.hotelmanager.validation.PageableValidator;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.hotelmanager.exception.ExceptionMessages.ROOM_NOT_FOUND_ID;
import static com.hotelmanager.exception.ExceptionMessages.ROOM_NUMBER_EXISTS;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;
    private final FilesService filesService;
    private final RoomTypeService roomTypeService;
    private final UserService userService;

    @Override
    public RoomResponseDto createRoom(RoomCreationDto creationDto) {
        validateRoomNumber(creationDto.getRoomNumber());

        Room newRoom = this.modelMapper.map(creationDto, Room.class);

        RoomType roomType = this.roomTypeService.getEntityByName(creationDto.getRoomType());
        newRoom.setRoomType(roomType);

        List<BedType> bedTypes = creationDto.getBedTypes();
        newRoom.setBedTypes(bedTypes);

        User user = this.userService.getAuthenticationUser();
        newRoom.setCreatedBy(user);

        Room createdRoom = this.roomRepository.save(newRoom);
        return buildRoomResponse(createdRoom, List.of(), List.of());
    }

    @Override
    public RoomResponseDto updateRoom(String id, RoomUpdateDto roomUpdateDto) {
        Room existingRoom = this.roomRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RoomNotFoundException(ROOM_NOT_FOUND_ID + id));

        if (!existingRoom.getRoomNumber().equals(roomUpdateDto.getRoomNumber())) {
            validateRoomNumber(roomUpdateDto.getRoomNumber());
            existingRoom.setRoomNumber(roomUpdateDto.getRoomNumber());
        }

        if (!existingRoom.getRoomType().getName().equalsIgnoreCase(roomUpdateDto.getRoomType())) {
            RoomType newRoomType = this.roomTypeService.getEntityByName(roomUpdateDto.getRoomType());
            existingRoom.setRoomType(newRoomType);
        }

        if (!existingRoom.getBedTypes().equals(roomUpdateDto.getBedTypes())) {
            existingRoom.setBedTypes(roomUpdateDto.getBedTypes());
        }

        if (existingRoom.getRoomStatus() != roomUpdateDto.getRoomStatus()) {
            existingRoom.setRoomStatus(roomUpdateDto.getRoomStatus());
        }

        Room updatedRoom = this.roomRepository.save(existingRoom);
        return buildRoomResponse(updatedRoom, List.of(), List.of());
    }

    @Transactional
    @Override
    public RoomResponseDto getRoomById(String id) {
        Room room = this.roomRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RoomNotFoundException(ROOM_NOT_FOUND_ID + id));

        List<RoomPhotoSummaryDto> photosByRoom = new ArrayList<>();
        List<RoomPhotoSummaryDto> photosByType = new ArrayList<>();
        try {
            log.info("Fetch room '{}' pictures from files service", id);
            photosByRoom = this.filesService.getPhotosByRoom(room.getUuid().toString())
                    .stream()
                    .map(photo -> this.modelMapper.map(photo, RoomPhotoSummaryDto.class))
                    .toList();

            log.info("Fetch room type '{}' pictures from files service", room.getRoomType().getName());
            photosByType = this.filesService.getPhotosByRoomType(room.getRoomType().getUuid().toString())
                    .stream()
                    .map(photo -> this.modelMapper.map(photo, RoomPhotoSummaryDto.class))
                    .toList();
        } catch (FeignException e) {
            log.warn("Failed to fetch pictures for room '{}': ", id, e);
        }

        return buildRoomResponse(room, photosByRoom, photosByType);
    }

    @Transactional
    @Override
    public Page<RoomPageResponseDto> getAllRooms(Optional<UUID> roomType, Optional<RoomStatus> roomStatus, Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.ASC, "roomNumber")
        );

        Specification<Room> spec = Specification.allOf(
                roomType.map(RoomSpecifications::byRoomType).orElse(null),
                roomStatus.map(RoomSpecifications::byRoomStatus).orElse(null)
        );

        Page<Room> rooms = roomRepository.findAll(spec, sortedPageable);

        PageableValidator.validatePageRequest(rooms, sortedPageable);

        return rooms.map(room -> {
            RoomType type = room.getRoomType();

            return RoomPageResponseDto.builder()
                    .uuid(room.getUuid().toString())
                    .roomNumber(room.getRoomNumber())
                    .roomType(type.getName())
                    .capacity(type.getCapacity())
                    .bedTypes(room.getBedTypes())
                    .pricePerNight(type.getBasePricePerNight())
                    .description(type.getDescription())
                    .roomStatus(room.getRoomStatus())
                    .build();
        });
    }

    @Override
    public void deleteRoomById(String id) {
        if (!this.roomRepository.existsById(UUID.fromString(id))) {
            throw new RoomNotFoundException(ROOM_NOT_FOUND_ID + id);
        }

        this.roomRepository.deleteById(UUID.fromString(id));

        try {
            this.filesService.deletePhotoByRoom(id);
        } catch (FeignException e) {
            log.warn("Failed to delete photos for room with id '{}': ", id, e);
        }
    }

    private void validateRoomNumber(String roomNumber) {
        if (this.roomRepository.existsByRoomNumber(roomNumber)) {
            throw new RoomNumberAlreadyExistsException(ROOM_NUMBER_EXISTS.formatted(roomNumber));
        }
    }

    private RoomResponseDto buildRoomResponse(Room room, List<RoomPhotoSummaryDto> photos, List<RoomPhotoSummaryDto> typePhotos) {
        RoomType roomType = room.getRoomType();
        List<String> bedTypes = room.getBedTypes().stream()
                .map(Objects::toString)
                .toList();

        return RoomResponseDto.builder()
                .uuid(room.getUuid())
                .roomNumber(room.getRoomNumber())
                .roomType(roomType.getName())
                .capacity(roomType.getCapacity())
                .bedTypes(bedTypes)
                .pricePerNight(roomType.getBasePricePerNight())
                .description(roomType.getDescription())
                .roomStatus(room.getRoomStatus())
                .createdAt(room.getCreatedDateTime())
                .updatedAt(room.getUpdatedDateTime())
                .photos(photos)
                .roomTypePhotos(typePhotos)
                .build();
    }
}
