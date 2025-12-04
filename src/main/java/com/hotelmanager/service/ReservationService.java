package com.hotelmanager.service;

import com.hotelmanager.model.dto.request.ReservationCreationDto;
import com.hotelmanager.model.dto.response.ReservationDetailsDto;
import com.hotelmanager.model.dto.response.ReservationPageResponseDto;
import com.hotelmanager.model.entity.Reservation;
import com.hotelmanager.model.enums.ReservationStatus;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface ReservationService {

    UUID createReservation(ReservationCreationDto reservationCreationDto);

    Reservation getReservationEntity(String reservationId);

    ReservationDetailsDto getReservationById(String id);

    Page<ReservationPageResponseDto> getAllReservations(Optional<ReservationStatus> status, Optional<LocalDate> fromDate, Optional<LocalDate> toDate, String sortBy, String direction, int page, int size);
}
