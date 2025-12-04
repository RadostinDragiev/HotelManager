package com.hotelmanager.service;

import com.hotelmanager.model.dto.request.ReservationCreationDto;
import com.hotelmanager.model.dto.response.ReservationDetailsDto;
import com.hotelmanager.model.entity.Reservation;

import java.util.UUID;

public interface ReservationService {

    UUID createReservation(ReservationCreationDto reservationCreationDto);

    Reservation getReservationEntity(String reservationId);

    ReservationDetailsDto getReservationById(String id);
}
