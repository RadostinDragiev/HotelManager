package com.hotelmanager.web;

import com.hotelmanager.model.dto.request.ReservationCreationDto;
import com.hotelmanager.service.ReservationService;
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
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'MANAGER', 'RECEPTIONIST')")
    @PostMapping
    public ResponseEntity<Void> createReservation(@Valid @RequestBody ReservationCreationDto reservationCreationDto,
                                                  UriComponentsBuilder uriComponentsBuilder) {
        UUID reservationId = this.reservationService.createReservation(reservationCreationDto);

        return ResponseEntity.created(uriComponentsBuilder
                        .path("/reservations/{id}")
                        .buildAndExpand(reservationId)
                        .toUri())
                .build();
    }
}
