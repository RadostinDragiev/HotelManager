-- PAYMENTS
CREATE TABLE payments
(
    uuid              UUID           NOT NULL,
    amount            DECIMAL(19, 2) NOT NULL,
    payment_type      VARCHAR(50)    NOT NULL,
    reason            VARCHAR(50)    NOT NULL,
    status            VARCHAR(50)    NOT NULL,
    notes             VARCHAR(255)   NULL,
    reservation_id    UUID           NULL,
    room_id           UUID           NULL,
    created_date_time DATETIME(6)    NOT NULL,
    updated_date_time DATETIME(6)    NOT NULL,

    CONSTRAINT pk_payments PRIMARY KEY (uuid),

    CONSTRAINT fk_payments_reservation
        FOREIGN KEY (reservation_id) REFERENCES reservations (uuid),

    CONSTRAINT fk_payments_room
        FOREIGN KEY (room_id) REFERENCES rooms (uuid)
);

CREATE INDEX idx_payments_reservation ON payments (reservation_id);
CREATE INDEX idx_payments_room ON payments (room_id);
