package com.hotelmanager.service;

import com.hotelmanager.model.dto.request.PaymentCreationDto;

import java.util.UUID;

public interface PaymentService {

    UUID createPayment(PaymentCreationDto paymentCreationDto);
}
