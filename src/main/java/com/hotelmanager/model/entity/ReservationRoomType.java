package com.hotelmanager.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reservations_room_types")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRoomType {

    @EmbeddedId
    @Builder.Default
    private ReservationRoomTypeId id = new ReservationRoomTypeId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("reservationId")
    @JoinColumn(name = "reservation_id", nullable = false, referencedColumnName = "uuid")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roomTypeId")
    @JoinColumn(name = "room_type_id", nullable = false, referencedColumnName = "uuid")
    private RoomType roomType;

    @Column(name = "rooms_count", nullable = false)
    private int roomsCount;
}
