package com.hotelmanager.web;

import com.hotelmanager.model.dto.request.RoomCreationDto;
import com.hotelmanager.model.dto.request.RoomUpdateDto;
import com.hotelmanager.model.dto.response.PageResponse;
import com.hotelmanager.model.dto.response.RoomPageResponseDto;
import com.hotelmanager.model.dto.response.RoomResponseDto;
import com.hotelmanager.model.enums.RoomStatus;
import com.hotelmanager.model.enums.RoomType;
import com.hotelmanager.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<Void> createRoom(@Valid @RequestBody RoomCreationDto creationDto, UriComponentsBuilder uriComponentsBuilder) {
        RoomResponseDto room = this.roomService.createRoom(creationDto);

        return ResponseEntity.created(uriComponentsBuilder
                        .path("/rooms/{id}")
                        .buildAndExpand(room.getUuid())
                        .toUri())
                .build();
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<RoomResponseDto> updateRoom(@PathVariable String id, @Valid @RequestBody RoomUpdateDto roomUpdateDto) {
        return ResponseEntity.ok(this.roomService.updateRoom(id, roomUpdateDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponseDto> getRoomById(@PathVariable String id) {
        return ResponseEntity.ok(this.roomService.getRoomById(id));
    }

    @GetMapping
    public PageResponse<RoomPageResponseDto> getAllRooms(@RequestParam Optional<RoomType> roomType,
                                                         @RequestParam Optional<RoomStatus> roomStatus,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RoomPageResponseDto> result = roomService.getAllRooms(roomType, roomStatus, pageable);

        return new PageResponse<>(
                result.getContent(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomById(@PathVariable String id) {
        this.roomService.deleteRoomById(id);
        return ResponseEntity.ok().build();
    }
}
