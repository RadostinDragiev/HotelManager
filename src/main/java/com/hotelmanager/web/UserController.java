package com.hotelmanager.web;

import com.hotelmanager.model.dto.request.UserDto;
import com.hotelmanager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserDto userDto, UriComponentsBuilder uriComponentsBuilder) {
        UUID userId = this.userService.createUser(userDto);

        return ResponseEntity.created(uriComponentsBuilder
                        .path("/users/{id}")
                        .buildAndExpand(userId)
                        .toUri())
                .build();
    }
}
