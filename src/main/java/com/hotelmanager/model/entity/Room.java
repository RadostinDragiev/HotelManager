package com.hotelmanager.model.entity;

import com.hotelmanager.model.enums.BedType;
import com.hotelmanager.model.enums.RoomStatus;
import com.hotelmanager.model.enums.RoomType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room extends BaseUUIDEntity {

    @Column(name = "room_number", unique = true, nullable = false)
    private String roomNumber;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @ElementCollection(targetClass = BedType.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "room_beds", joinColumns = @JoinColumn(name = "room_id"))
    @Enumerated(EnumType.STRING)
    private List<BedType> bedTypes;

    @Column(name = "price_per_night", nullable = false)
    private BigDecimal pricePerNight;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private RoomStatus roomStatus;

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
