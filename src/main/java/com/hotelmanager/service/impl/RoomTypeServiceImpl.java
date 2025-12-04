package com.hotelmanager.service.impl;

import com.hotelmanager.exception.exceptions.RoomTypeAlreadyExistsException;
import com.hotelmanager.exception.exceptions.RoomTypeNotFoundException;
import com.hotelmanager.model.dto.RoomTypeAvailability;
import com.hotelmanager.model.dto.request.RoomTypeCreationDto;
import com.hotelmanager.model.dto.response.ImageResponseDto;
import com.hotelmanager.model.dto.response.RoomTypeDto;
import com.hotelmanager.model.dto.response.RoomTypesPreview;
import com.hotelmanager.model.entity.RoomType;
import com.hotelmanager.repository.RoomTypeRepository;
import com.hotelmanager.service.FilesService;
import com.hotelmanager.service.RoomTypeService;
import com.hotelmanager.service.UserService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.hotelmanager.exception.ExceptionMessages.ROOM_TYPE_EXISTS;
import static com.hotelmanager.exception.ExceptionMessages.ROOM_TYPE_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomTypeServiceImpl implements RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;
    private final ModelMapper modelMapper;
    private final FilesService filesService;
    private final UserService userService;

    @Override
    public RoomType getEntityByName(String name) {
        return this.roomTypeRepository.getByName(name).orElseThrow(() -> new RoomTypeNotFoundException(ROOM_TYPE_NOT_FOUND));
    }

    @Override
    public RoomTypeDto createRoomType(RoomTypeCreationDto creationDto, MultipartFile[] images) {
        if (this.roomTypeRepository.existsByName(creationDto.getName())) {
            throw new RoomTypeAlreadyExistsException(ROOM_TYPE_EXISTS);
        }

        RoomType roomType = this.modelMapper.map(creationDto, RoomType.class);
        roomType.setCreatedBy(this.userService.getAuthenticationUser());

        RoomType createdRoomType = this.roomTypeRepository.save(roomType);
        List<ImageResponseDto> uploadedImages = uploadImages(createdRoomType.getUuid(), images);

        RoomTypeDto roomTypeDto = this.modelMapper.map(createdRoomType, RoomTypeDto.class);
        roomTypeDto.setImages(uploadedImages);
        return roomTypeDto;
    }

    @Override
    public List<RoomTypeDto> getAllTypes() {
        return this.roomTypeRepository.findAll(Sort.by("name"))
                .stream()
                .map(roomType -> {
                    RoomTypeDto roomTypeDto = this.modelMapper.map(roomType, RoomTypeDto.class);

                    List<ImageResponseDto> images = new ArrayList<>();
                    try {
                        images = this.filesService.getPhotosByRoomType(roomType.getUuid().toString())
                                .stream()
                                .map(image -> this.modelMapper.map(image, ImageResponseDto.class))
                                .toList();
                    } catch (FeignException e) {
                        log.warn("Failed to retrieve room type images! ", e);
                    }

                    roomTypeDto.setImages(images);
                    return roomTypeDto;
                })
                .toList();
    }

    @Override
    public List<RoomTypesPreview> getTypesPreview() {
        return this.roomTypeRepository.findAll().stream()
                .map(roomType -> this.modelMapper.map(roomType, RoomTypesPreview.class))
                .toList();
    }

    @Override
    public Map<String, RoomTypeAvailability> roomTypeAvailabilitiesMap(LocalDate startDate, LocalDate endDate) {
        return this.roomTypeRepository
                .availableRoomsByType(startDate, endDate)
                .stream()
                .collect(Collectors.toMap(RoomTypeAvailability::roomType, roomType -> roomType));
    }

    private List<ImageResponseDto> uploadImages(UUID roomTypeId, MultipartFile[] images) {
        List<ImageResponseDto> imageResponseDtos = new ArrayList<>();

        try {
            imageResponseDtos = this.filesService.uploadRoomTypeImages(roomTypeId.toString(), images)
                    .stream()
                    .map(image -> this.modelMapper.map(image, ImageResponseDto.class))
                    .toList();
        } catch (FeignException e) {
            log.warn("Failed to upload room type images! ", e);
        }
        return imageResponseDtos;
    }
}
