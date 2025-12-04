package com.hotelmanager.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hotelmanager.model.enums.ReservationStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDetailsDto {

    private UUID uuid;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private int guestsCount;
    private ReservationStatus reservationStatus;
    private BigDecimal reservationCoast;
    private BigDecimal payedAmount;
    private BigDecimal pendingAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<ReservationRoomTypeDto> roomTypes;
    private List<ReservationRoomDto> rooms;
    private List<ReservationPaymentDto> payments;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDateTime;
}
