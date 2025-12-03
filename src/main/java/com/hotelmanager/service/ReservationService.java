package com.hotelmanager.service;

import com.hotelmanager.model.dto.request.ReservationCreationDto;

import java.util.UUID;

public interface ReservationService {

    UUID createReservation(ReservationCreationDto reservationCreationDto);
}
