package com.hotelmanager.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hotelmanager.model.enums.ReservationPaymentType;
import com.hotelmanager.validation.annotation.ValidDateRange;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

import static com.hotelmanager.validation.ValidationMessages.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidDateRange(
        start = "startDate",
        end = "endDate",
        message = "Start date must be before end date!"
)
public class ReservationCreationDto {

    @NotNull(message = FIRST_NAME_NOT_NULL)
    @Size(min = 2, max = 50, message = FIRST_NAME_SIZE)
    private String firstName;

    @NotNull(message = LAST_NAME_NOT_NULL)
    @Size(min = 2, max = 50, message = LAST_NAME_SIZE)
    private String lastName;

    @NotNull(message = EMAIL_NOT_NULL)
    @Email(message = VALID_EMAIL)
    private String email;

    @NotNull(message = PHONE_NOT_NULL)
    private String phone;

    @Positive(message = GUESTS_COUNT_ONLY_POSITIVE)
    private int guestsCount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = START_DATE_NOT_NULL)
    @FutureOrPresent(message = START_DATE_NOT_PAST)
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = END_DATE_NOT_NULL)
    @FutureOrPresent(message = END_DATE_NOT_PAST)
    private LocalDate endDate;

    @NotNull(message = RESERVATION_PAYMENT_TYPE_NOT_NULL)
    private ReservationPaymentType reservationPaymentType;

    @NotEmpty(message = ROOMS_LIST_NOT_EMPTY)
    private @Valid List<ReservationRoomDto> rooms;
}
