package com.hotelmanager.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hotelmanager.model.enums.PaymentReason;
import com.hotelmanager.model.enums.PaymentStatus;
import com.hotelmanager.model.enums.PaymentType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {

    private BigDecimal amount;
    private PaymentType paymentType;
    private PaymentReason reason;
    private PaymentStatus status;
    private String notes;
    private String reservationUUID;
    private String roomNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDateTime;
}
