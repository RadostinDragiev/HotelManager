package com.hotelmanager.web;

import com.hotelmanager.model.dto.request.UserDto;
import com.hotelmanager.model.dto.response.PageResponse;
import com.hotelmanager.model.dto.response.UserDetailsDto;
import com.hotelmanager.model.dto.response.UserPageDto;
import com.hotelmanager.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@PreAuthorize("hasAnyRole('ADMINISTRATOR', 'MANAGER')")
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserDto userDto, UriComponentsBuilder uriComponentsBuilder) {
        UUID userId = this.userService.createUser(userDto);

        return ResponseEntity.created(uriComponentsBuilder
                        .path("/users/{id}")
                        .buildAndExpand(userId)
                        .toUri())
                .build();
    }

    @GetMapping
    public ResponseEntity<PageResponse<UserPageDto>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserPageDto> allUsers = this.userService.getAllUsers(pageable);
        PageResponse<UserPageDto> usersPageResponse = new PageResponse<>(
                allUsers.getContent(),
                allUsers.getNumber(),
                allUsers.getSize(),
                allUsers.getTotalElements(),
                allUsers.getTotalPages()
        );

        return ResponseEntity.ok(usersPageResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailsDto> getUserById(@NotNull @PathVariable String id) {
        return ResponseEntity.ok(this.userService.getUserById(id));
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable String id) {
        this.userService.activateUser(id);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateUser(@PathVariable String id) {
        this.userService.deactivateUser(id);

        return ResponseEntity.ok().build();
    }
}
