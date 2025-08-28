package com.hotelmanager.web;

import com.hotelmanager.model.dto.RoomCreationDto;
import com.hotelmanager.model.dto.RoomResponseDto;
import com.hotelmanager.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<Void> createRoom(@Valid @RequestBody RoomCreationDto creationDto, UriComponentsBuilder uriComponentsBuilder) {
        RoomResponseDto room = this.roomService.createRoom(creationDto);

        return ResponseEntity.created(uriComponentsBuilder
                        .path("/rooms/{id}")
                        .buildAndExpand(room.getUuid())
                        .toUri())
                .build();
    }
}
