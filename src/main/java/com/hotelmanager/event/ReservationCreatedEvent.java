package com.hotelmanager.event;

import com.hotelmanager.model.enums.ReservationPaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ReservationCreatedEvent {

    private final String reservationId;
    private final ReservationPaymentType reservationPaymentType;
    private final BigDecimal accommodationCoast;
}
