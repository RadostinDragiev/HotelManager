ALTER TABLE reservations
    ADD COLUMN reservation_payment_type VARCHAR(50) NOT NULL AFTER accommodation_coast;
