package com.hotelmanager.web;

import com.hotelmanager.model.dto.request.RoomTypeCreationDto;
import com.hotelmanager.model.dto.response.RoomTypeDto;
import com.hotelmanager.service.RoomTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/room-type")
@RequiredArgsConstructor
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<RoomTypeDto> createRoomType(@Valid @ModelAttribute("creationDto") RoomTypeCreationDto creationDto,
                                                      @RequestParam("images") MultipartFile[] images,
                                                      UriComponentsBuilder uriComponentsBuilder) {
        RoomTypeDto roomType = this.roomTypeService.createRoomType(creationDto, images);

        return ResponseEntity.created(uriComponentsBuilder
                        .path("/room-type/{id}")
                        .buildAndExpand(roomType.getUuid())
                        .toUri())
                .build();
    }

    @GetMapping
    public ResponseEntity<List<RoomTypeDto>> getAllTypes() {
        return ResponseEntity.ok(this.roomTypeService.getAllTypes());
    }
}
