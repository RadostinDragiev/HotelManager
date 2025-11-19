package com.hotelmanager.web;

import com.hotelmanager.model.dto.request.UserDto;
import com.hotelmanager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'MANAGER')")
    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserDto userDto, UriComponentsBuilder uriComponentsBuilder) {
        UUID userId = this.userService.createUser(userDto);

        return ResponseEntity.created(uriComponentsBuilder
                        .path("/users/{id}")
                        .buildAndExpand(userId)
                        .toUri())
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'MANAGER')")
    @PostMapping("/{id}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable String id) {
        this.userService.activateUser(id);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateUser(@PathVariable String id) {
        this.userService.deactivateUser(id);

        return ResponseEntity.ok().build();
    }
}
