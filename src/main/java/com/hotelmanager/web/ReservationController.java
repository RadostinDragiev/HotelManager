package com.hotelmanager.web;

import com.hotelmanager.model.dto.request.ReservationCreationDto;
import com.hotelmanager.model.dto.response.ReservationDetailsDto;
import com.hotelmanager.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'MANAGER', 'RECEPTIONIST')")
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDetailsDto> getReservationById(@PathVariable String id) {
        ReservationDetailsDto reservationDetails = this.reservationService.getReservationById(id);

        return ResponseEntity.ok(reservationDetails);
    }
}
