package com.hotelmanager.service;

import com.hotelmanager.model.dto.request.RoomTypeCreationDto;
import com.hotelmanager.model.dto.response.RoomTypeDto;
import com.hotelmanager.model.entity.RoomType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RoomTypeService {

    RoomType getEntityByName(String name);

    RoomTypeDto createRoomType(RoomTypeCreationDto creationDto, MultipartFile[] images);

    List<RoomTypeDto> getAllTypes();
}
