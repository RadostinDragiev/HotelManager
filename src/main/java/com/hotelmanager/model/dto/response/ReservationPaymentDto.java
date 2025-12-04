package com.hotelmanager.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hotelmanager.model.enums.PaymentReason;
import com.hotelmanager.model.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationPaymentDto {

    private UUID uuid;
    private BigDecimal amount;
    private PaymentReason reason;
    private PaymentStatus status;
    private UUID roomId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDateTime;
}
