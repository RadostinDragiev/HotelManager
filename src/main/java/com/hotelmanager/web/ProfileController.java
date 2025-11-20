package com.hotelmanager.web;

import com.hotelmanager.model.dto.request.ProfilePasswordDto;
import com.hotelmanager.model.dto.response.ProfileDto;
import com.hotelmanager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ProfileDto> getProfile() {
        return ResponseEntity.ok(this.userService.getUserProfile());
    }

    @PostMapping("/password")
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody ProfilePasswordDto profilePasswordDto) {
        this.userService.updateProfilePassword(profilePasswordDto);

        return ResponseEntity.ok().build();
    }
}
