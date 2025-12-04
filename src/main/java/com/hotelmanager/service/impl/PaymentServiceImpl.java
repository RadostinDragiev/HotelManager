package com.hotelmanager.service.impl;

import com.hotelmanager.event.ReservationCreatedEvent;
import com.hotelmanager.exception.exceptions.InvalidReservationPaymentTypeException;
import com.hotelmanager.exception.exceptions.PaymentNotFoundException;
import com.hotelmanager.model.dto.request.PaymentCreationDto;
import com.hotelmanager.model.dto.response.PaymentMenus;
import com.hotelmanager.model.dto.response.PaymentResponseDto;
import com.hotelmanager.model.entity.Payment;
import com.hotelmanager.model.entity.Reservation;
import com.hotelmanager.model.entity.Room;
import com.hotelmanager.model.enums.PaymentReason;
import com.hotelmanager.model.enums.PaymentStatus;
import com.hotelmanager.model.enums.PaymentType;
import com.hotelmanager.repository.PaymentRepository;
import com.hotelmanager.service.PaymentService;
import com.hotelmanager.service.ReservationService;
import com.hotelmanager.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import static com.hotelmanager.exception.ExceptionMessages.INVALID_RESERVATION_PAYMENT_TYPE;
import static com.hotelmanager.exception.ExceptionMessages.PAYMENT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationService reservationService;
    private final RoomService roomService;
    private final ModelMapper modelMapper;

    @Override
    public UUID createPayment(PaymentCreationDto paymentCreationDto) {

        Payment payment = this.modelMapper.map(paymentCreationDto, Payment.class);

        Reservation reservation = getReservationById(paymentCreationDto.getReservationId());
        payment.setReservation(reservation);

        if (paymentCreationDto.getRoomId() != null) {
            Room room = this.roomService.getRoomEntityById(paymentCreationDto.getRoomId());
            payment.setRoom(room);
        }

        Payment createdPayment = this.paymentRepository.save(payment);
        return createdPayment.getUuid();
    }

    @Override
    public PaymentMenus getPaymentMenus() {
        return PaymentMenus.builder()
                .paymentTypes(Arrays.stream(PaymentType.values()).toList())
                .reasons(Arrays.stream(PaymentReason.values()).toList())
                .status(Arrays.stream(PaymentStatus.values()).toList())
                .build();
    }

    @Override
    public PaymentResponseDto getPaymentById(String id) {
        Payment payment = this.paymentRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new PaymentNotFoundException(PAYMENT_NOT_FOUND));
        return this.modelMapper.map(payment, PaymentResponseDto.class);
    }

    @EventListener
    public void createReservationPayment(ReservationCreatedEvent event) {
        BigDecimal amount = event.getAccommodationCoast();
        PaymentType paymentType = PaymentType.CARD_PAYMENT;
        PaymentReason reason;

        switch (event.getReservationPaymentType()) {
            case FULL_PREPAY -> reason = PaymentReason.ACCOMMODATION_PREPAID;
            case RESERVATION_DEPOSIT -> {
                amount = event.getAccommodationCoast().multiply(BigDecimal.valueOf(0.30));
                reason = PaymentReason.DEPOSIT;
            }
            case PAY_AT_PROPERTY -> {
                paymentType = PaymentType.NOT_SPECIFIED;
                reason = PaymentReason.PAY_AT_PROPERTY_ACCOMMODATION;
            }
            default ->
                    throw new InvalidReservationPaymentTypeException(INVALID_RESERVATION_PAYMENT_TYPE.formatted(event.getReservationPaymentType()));
        }

        Payment payment = Payment.builder()
                .amount(amount)
                .paymentType(paymentType)
                .reason(reason)
                .status(PaymentStatus.PENDING)
                .reservation(getReservationById(event.getReservationId()))
                .build();

        this.paymentRepository.save(payment);
    }

    private Reservation getReservationById(String reservationId) {
        return this.reservationService.getReservationEntity(reservationId);
    }
}
