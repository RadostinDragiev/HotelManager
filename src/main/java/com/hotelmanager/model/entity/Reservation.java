package com.hotelmanager.model.entity;

import com.hotelmanager.model.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation extends BaseUUIDEntity {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "guests_count", nullable = false)
    private int guestsCount;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    @Column(name = "accommodation_coast", nullable = false)
    private BigDecimal accommodationCoast;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ReservationRoomType> roomTypes = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "reservations_rooms",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id")
    )
    private Set<Room> rooms;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @CreationTimestamp
    @Column(name = "created_date_time", nullable = false, updatable = false)
    private LocalDateTime createdDateTime;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "uuid")
    private User createdBy;

    @UpdateTimestamp
    @Column(name = "updated_date_time", nullable = false, updatable = false)
    private LocalDateTime updatedDateTime;

    @ManyToOne
    @JoinColumn(name = "updated_by", referencedColumnName = "uuid")
    private User updatedBy;
}
