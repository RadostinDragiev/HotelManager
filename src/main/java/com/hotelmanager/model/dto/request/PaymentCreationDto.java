package com.hotelmanager.model.dto.request;

import com.hotelmanager.model.enums.PaymentReason;
import com.hotelmanager.model.enums.PaymentStatus;
import com.hotelmanager.model.enums.PaymentType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

import static com.hotelmanager.validation.ValidationMessages.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCreationDto {

    @NotNull(message = AMOUNT_NOT_NULL)
    @Positive(message = AMOUNT_ONLY_POSITIVE)
    private BigDecimal amount;

    @NotNull(message = PAYMENT_TYPE_NOT_NULL)
    private PaymentType paymentType;

    @NotNull(message = REASON_NOT_NULL)
    private PaymentReason reason;

    @NotNull(message = PAYMENT_STATUS_NOT_NULL)
    private PaymentStatus paymentStatus;

    private String notes;

    @NotNull(message = RESERVATION_ID_NOT_NULL)
    private String reservationId;

    private String roomId;
}
