package com.hotelmanager.web;

import com.hotelmanager.model.dto.request.RoomCreationDto;
import com.hotelmanager.model.dto.request.RoomUpdateDto;
import com.hotelmanager.model.dto.response.RoomResponseDto;
import com.hotelmanager.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PutMapping("/{id}")
    public ResponseEntity<RoomResponseDto> updateRoom(@PathVariable String id, @Valid @RequestBody RoomUpdateDto roomUpdateDto) {
        return ResponseEntity.ok(this.roomService.updateRoom(id, roomUpdateDto));
    }
}
