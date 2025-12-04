package com.hotelmanager.web;

import com.hotelmanager.model.dto.request.ReservationCreationDto;
import com.hotelmanager.model.dto.response.PageResponse;
import com.hotelmanager.model.dto.response.ReservationDetailsDto;
import com.hotelmanager.model.dto.response.ReservationPageResponseDto;
import com.hotelmanager.model.enums.ReservationStatus;
import com.hotelmanager.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Optional;
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

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'MANAGER', 'RECEPTIONIST')")
    @GetMapping
    public PageResponse<ReservationPageResponseDto> getAllReservations(@RequestParam Optional<ReservationStatus> status,
                                                                       @RequestParam Optional<LocalDate> fromDate,
                                                                       @RequestParam Optional<LocalDate> toDate,
                                                                       @RequestParam(defaultValue = "startDate") String sortBy,
                                                                       @RequestParam(defaultValue = "asc") String direction,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size) {
        Page<ReservationPageResponseDto> reservations = this.reservationService.getAllReservations(status, fromDate, toDate, sortBy, direction, page, size);

        return new PageResponse<>(
                reservations.getContent(),
                reservations.getNumber(),
                reservations.getSize(),
                reservations.getTotalElements(),
                reservations.getTotalPages()
        );
    }
}
