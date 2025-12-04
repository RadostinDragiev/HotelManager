package com.hotelmanager.service.impl;

import com.hotelmanager.exception.exceptions.NotEnoughRoomsAvailableException;
import com.hotelmanager.exception.exceptions.ReservationNotFoundException;
import com.hotelmanager.model.dto.RoomTypeAvailability;
import com.hotelmanager.model.dto.request.ReservationCreationDto;
import com.hotelmanager.model.dto.request.ReservationRoomDto;
import com.hotelmanager.model.dto.response.ReservationDetailsDto;
import com.hotelmanager.model.dto.response.ReservationPageResponseDto;
import com.hotelmanager.model.dto.response.ReservationPaymentDto;
import com.hotelmanager.model.entity.Reservation;
import com.hotelmanager.model.entity.ReservationRoomType;
import com.hotelmanager.model.entity.RoomType;
import com.hotelmanager.model.entity.User;
import com.hotelmanager.model.enums.PaymentStatus;
import com.hotelmanager.model.enums.ReservationStatus;
import com.hotelmanager.repository.ReservationRepository;
import com.hotelmanager.service.ReservationService;
import com.hotelmanager.service.RoomTypeService;
import com.hotelmanager.service.UserService;
import com.hotelmanager.specifications.ReservationSpecifications;
import com.hotelmanager.validation.PageableValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.hotelmanager.exception.ExceptionMessages.NOT_ENOUGH_ROOMS_AVAILABLE;
import static com.hotelmanager.exception.ExceptionMessages.RESERVATION_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final RoomTypeService roomTypeService;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public UUID createReservation(ReservationCreationDto reservationCreationDto) {
        LocalDate startDate = reservationCreationDto.getStartDate();
        LocalDate endDate = reservationCreationDto.getEndDate();
        List<ReservationRoomDto> rooms = reservationCreationDto.getRooms();

        validateCapacity(startDate, endDate, rooms);

        BigDecimal accommodationCoast = calculateAccommodationCost(rooms, startDate, endDate);

        User user = this.userService.getAuthenticationUser();

        Reservation reservation = Reservation.builder()
                .firstName(reservationCreationDto.getFirstName())
                .lastName(reservationCreationDto.getLastName())
                .email(reservationCreationDto.getEmail())
                .phone(reservationCreationDto.getPhone())
                .guestsCount(reservationCreationDto.getGuestsCount())
                .reservationStatus(ReservationStatus.RESERVATION_REQUEST)
                .accommodationCoast(accommodationCoast)
                .startDate(startDate)
                .endDate(endDate)
                .createdBy(user)
                .build();

        Set<ReservationRoomType> reservationRoomTypes = getReservationRoomTypes(reservation, rooms);
        reservation.setRoomTypes(reservationRoomTypes);

        Reservation createdReservation = this.reservationRepository.save(reservation);

        return createdReservation.getUuid();
    }

    @Override
    public Reservation getReservationEntity(String reservationId) {
        return this.reservationRepository.findById(UUID.fromString(reservationId))
                .orElseThrow(() -> new ReservationNotFoundException(RESERVATION_NOT_FOUND));
    }

    @Transactional
    @Override
    public ReservationDetailsDto getReservationById(String id) {
        Reservation reservation = this.reservationRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ReservationNotFoundException(RESERVATION_NOT_FOUND));

        ReservationDetailsDto detailsDto = this.modelMapper.map(reservation, ReservationDetailsDto.class);

        calculateReservationDetailsCoasts(detailsDto, reservation.getAccommodationCoast());

        return detailsDto;
    }

    @Override
    public Page<ReservationPageResponseDto> getAllReservations(Optional<ReservationStatus> status, Optional<LocalDate> fromDate, Optional<LocalDate> toDate, String sortBy, String direction, int page, int size) {
        Pageable sortedPageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));

        Specification<Reservation> spec = Specification.allOf(
                status.map(ReservationSpecifications::byReservationStatus).orElse(null),
                ReservationSpecifications.betweenDate(fromDate.orElse(null), toDate.orElse(null))
        );

        Page<Reservation> reservations = this.reservationRepository.findAll(spec, sortedPageable);

        PageableValidator.validatePageRequest(reservations, sortedPageable);

        return reservations.map(reservation -> this.modelMapper.map(reservation, ReservationPageResponseDto.class));
    }

    private void validateCapacity(LocalDate startDate, LocalDate endDate, List<ReservationRoomDto> rooms) {
        Map<String, RoomTypeAvailability> roomTypeAvailabilitiesMap = this.roomTypeService.roomTypeAvailabilitiesMap(startDate, endDate);

        for (ReservationRoomDto room : rooms) {
            RoomTypeAvailability roomTypeAvailability = roomTypeAvailabilitiesMap.get(room.getRoomTypeName());

            if (roomTypeAvailability.availableRooms() - room.getRoomsCount() < 0) {
                throw new NotEnoughRoomsAvailableException(NOT_ENOUGH_ROOMS_AVAILABLE.formatted(room.getRoomTypeName()));
            }
        }
    }

    private BigDecimal calculateAccommodationCost(List<ReservationRoomDto> rooms,
                                                  LocalDate startDate,
                                                  LocalDate endDate) {

        long nights = ChronoUnit.DAYS.between(startDate, endDate);

        // TODO: Add discount for client with fully prepaid stay
//        BigDecimal percent = BigDecimal.valueOf(-5);
//        BigDecimal factor = BigDecimal.ONE.add(percent.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP));

        BigDecimal total = BigDecimal.ZERO;

        for (ReservationRoomDto reservationRoomDto : rooms) {

            RoomType type = roomTypeService.getEntityByName(reservationRoomDto.getRoomTypeName());

            BigDecimal basePrice = type.getBasePricePerNight();
            BigDecimal roomsCount = BigDecimal.valueOf(reservationRoomDto.getRoomsCount());

            BigDecimal priceForType = basePrice
                    .multiply(BigDecimal.valueOf(nights))
                    .multiply(roomsCount);
//                    .multiply(factor);

            total = total.add(priceForType);
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }

    public Set<ReservationRoomType> getReservationRoomTypes(Reservation reservation, List<ReservationRoomDto> rooms) {
        return rooms.stream()
                .map(reservationRoomDto ->
                        ReservationRoomType.builder()
                                .reservation(reservation)
                                .roomType(this.roomTypeService.getEntityByName(reservationRoomDto.getRoomTypeName()))
                                .roomsCount(reservationRoomDto.getRoomsCount())
                                .build())
                .collect(Collectors.toSet());
    }

    private void calculateReservationDetailsCoasts(ReservationDetailsDto detailsDto, BigDecimal accommodationCoast) {
        BigDecimal totalCost = accommodationCoast;
        BigDecimal paymentsMade = BigDecimal.ZERO;
        BigDecimal pendingAmount = BigDecimal.ZERO;

        for (ReservationPaymentDto paymentDto : detailsDto.getPayments()) {
            PaymentStatus status = paymentDto.getStatus();

            if (status == PaymentStatus.ACCEPTED) {
                paymentsMade = paymentsMade.add(paymentDto.getAmount());
            }

            if (status == PaymentStatus.PENDING) {
                pendingAmount = pendingAmount.add(paymentDto.getAmount());
            }

            if (status == PaymentStatus.ACCEPTED || status == PaymentStatus.PENDING) {
                totalCost = totalCost.add(paymentDto.getAmount());
            }
        }

        detailsDto.setReservationCoast(totalCost);
        detailsDto.setPayedAmount(paymentsMade);
        detailsDto.setPendingAmount(pendingAmount);
    }
}
