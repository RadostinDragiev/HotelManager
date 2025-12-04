package com.hotelmanager.service;

import com.hotelmanager.model.dto.RoomTypeAvailability;
import com.hotelmanager.model.dto.request.RoomTypeCreationDto;
import com.hotelmanager.model.dto.response.RoomTypeDto;
import com.hotelmanager.model.dto.response.RoomTypesPreview;
import com.hotelmanager.model.entity.RoomType;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface RoomTypeService {

    RoomTypeDto createRoomType(RoomTypeCreationDto creationDto, MultipartFile[] images);

    RoomType getEntityByName(String name);

    Map<String, RoomTypeAvailability> roomTypeAvailabilitiesMap(LocalDate startDate, LocalDate endDate);

    List<RoomTypeDto> getAllTypes();

    List<RoomTypesPreview> getTypesPreview();
}
