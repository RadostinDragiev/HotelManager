package com.hotelmanager.model.dto.response;

import com.hotelmanager.model.enums.ReservationStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationPageResponseDto {

    private UUID uuid;
    private String firstName;
    private String lastName;
    private int guestsCount;
    private ReservationStatus reservationStatus;
    private LocalDate startDate;
    private LocalDate endDate;
}
