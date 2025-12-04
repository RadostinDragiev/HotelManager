package com.hotelmanager.model.dto.response;

import com.hotelmanager.model.enums.PaymentReason;
import com.hotelmanager.model.enums.PaymentStatus;
import com.hotelmanager.model.enums.PaymentType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMenus {

    List<PaymentType> paymentTypes;
    List<PaymentReason> reasons;
    List<PaymentStatus> status;
}
