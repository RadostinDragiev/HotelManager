-- ROOM_TYPES
CREATE TABLE room_types
(
    uuid                 UUID           NOT NULL,
    name                 VARCHAR(255)   NOT NULL UNIQUE,
    base_price_per_night DECIMAL(19, 2) NOT NULL,
    capacity             INT            NOT NULL,
    description          TEXT,
    created_date_time    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by           UUID           NULL,
    updated_date_time    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by           UUID           NULL,

    PRIMARY KEY (uuid),

    CONSTRAINT fk_room_types_created_by
        FOREIGN KEY (created_by) REFERENCES users (uuid),
    CONSTRAINT fk_room_types_updated_by
        FOREIGN KEY (updated_by) REFERENCES users (uuid)
);

INSERT INTO room_types (uuid, name, base_price_per_night, capacity, description)
VALUES (UUID(), 'STANDARD_SINGLE_ROOM', '80', '1', 'Standard single person room'),
       (UUID(), 'STANDARD_DOUBLE_ROOM', '100', '2', 'Standard double person room'),
       (UUID(), 'DELUXE_SINGLE_ROOM', '140', '1', 'Deluxe single person room'),
       (UUID(), 'DELUXE_DOUBLE_ROOM', '160', '2', 'Deluxe double person room'),
       (UUID(), 'DELUXE_APARTMENT', '240', '4', 'Deluxe apartment for small family'),
       (UUID(), 'DELUXE_DOUBLE_APARTMENT', '300', '6', 'Deluxe apartment for big family');

ALTER TABLE rooms
    ADD COLUMN room_type_id UUID NOT NULL AFTER room_number;

ALTER TABLE rooms
    MODIFY COLUMN room_type_id UUID NOT NULL;

ALTER TABLE rooms
    DROP COLUMN room_type,
    DROP COLUMN capacity,
    DROP COLUMN price_per_night,
    DROP COLUMN description;

ALTER TABLE rooms
    ADD INDEX idx_rooms_room_type_id (room_type_id),
    ADD CONSTRAINT fk_rooms_room_type
        FOREIGN KEY (room_type_id) REFERENCES room_types (uuid);