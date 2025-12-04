package com.hotelmanager.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "room_types")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomType extends BaseUUIDEntity {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "base_price_per_night", nullable = false)
    private BigDecimal basePricePerNight;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "created_date_time", updatable = false)
    private LocalDateTime createdDateTime;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "uuid")
    private User createdBy;

    @UpdateTimestamp
    @Column(name = "updated_date_time", updatable = false)
    private LocalDateTime updatedDateTime;

    @ManyToOne
    @JoinColumn(name = "updated_by", referencedColumnName = "uuid")
    private User updatedBy;
}
