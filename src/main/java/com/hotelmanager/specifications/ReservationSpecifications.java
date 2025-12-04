package com.hotelmanager.specifications;

import com.hotelmanager.model.entity.Reservation;
import com.hotelmanager.model.enums.ReservationStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReservationSpecifications {

    public static Specification<Reservation> byReservationStatus(ReservationStatus status) {
        return ((root, query, cb) -> cb.equal(root.get("reservationStatus"), status));
    }

    public static Specification<Reservation> betweenDate(LocalDate from, LocalDate to) {
        return ((root, query, cb) -> {
            if (from == null && to == null) {
                return cb.conjunction();
            }

            if (from != null && to != null) {
                return cb.and(
                        cb.greaterThanOrEqualTo(root.get("startDate"), from),
                        cb.lessThanOrEqualTo(root.get("endDate"), to)
                );
            } else if (from != null) {
                return cb.greaterThanOrEqualTo(root.get("startDate"), from);
            } else {
                return cb.lessThanOrEqualTo(root.get("endDate"), to);
            }
        });
    }
}
