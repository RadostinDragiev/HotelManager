package com.hotelmanager.service.impl;

import com.hotelmanager.model.dto.request.PaymentCreationDto;
import com.hotelmanager.model.entity.Payment;
import com.hotelmanager.model.entity.Reservation;
import com.hotelmanager.model.entity.Room;
import com.hotelmanager.repository.PaymentRepository;
import com.hotelmanager.service.PaymentService;
import com.hotelmanager.service.ReservationService;
import com.hotelmanager.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

        Reservation reservation = this.reservationService.getReservationEntity(paymentCreationDto.getReservationId());
        payment.setReservation(reservation);

        if (paymentCreationDto.getRoomId() != null) {
            Room room = this.roomService.getRoomEntityById(paymentCreationDto.getRoomId());
            payment.setRoom(room);
        }

        Payment createdPayment = this.paymentRepository.save(payment);
        return createdPayment.getUuid();
    }
}
