package com.hotelmanager.service;

import com.hotelmanager.model.dto.request.PaymentCreationDto;
import com.hotelmanager.model.dto.response.PaymentMenus;
import com.hotelmanager.model.dto.response.PaymentResponseDto;

import java.util.UUID;

public interface PaymentService {

    UUID createPayment(PaymentCreationDto paymentCreationDto);

    PaymentMenus getPaymentMenus();

    PaymentResponseDto getPaymentById(String id);
}
