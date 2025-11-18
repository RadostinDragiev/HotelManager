package com.hotelmanager.web;

import com.hotelmanager.model.dto.request.AuthRequestDto;
import com.hotelmanager.model.dto.response.AuthResponseDto;
import com.hotelmanager.service.UserService;
import com.hotelmanager.service.impl.HotelUserDetails;
import com.hotelmanager.service.impl.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/login")
    public AuthResponseDto login(@Valid @RequestBody AuthRequestDto request) {
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()
                )
        );

        HotelUserDetails userDetails = (HotelUserDetails) authentication.getPrincipal();
        this.userService.updateLastLogin(userDetails.getUsername());
        String token = this.jwtService.generateToken(userDetails);
        return new AuthResponseDto(token, userDetails.getAsUserInfoDto());
    }
}
