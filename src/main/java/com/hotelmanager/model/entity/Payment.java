package com.hotelmanager.model.entity;

import com.hotelmanager.model.enums.PaymentReason;
import com.hotelmanager.model.enums.PaymentStatus;
import com.hotelmanager.model.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseUUIDEntity {

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private PaymentReason reason;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "notes")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "reservation_id", referencedColumnName = "uuid")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "uuid")
    private Room room;

    @CreationTimestamp
    @Column(name = "created_date_time", nullable = false, updatable = false)
    private LocalDateTime createdDateTime;

    @UpdateTimestamp
    @Column(name = "updated_date_time", nullable = false, updatable = false)
    private LocalDateTime updatedDateTime;
}
