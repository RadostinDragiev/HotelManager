package com.hotelmanager.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ReservationRoomTypeId implements Serializable {

    @Column(name = "reservation_id", nullable = false)
    private UUID reservationId;

    @Column(name = "room_type_id", nullable = false)
    private UUID roomTypeId;
}

