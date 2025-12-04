-- RESERVATIONS
CREATE TABLE reservations
(
    uuid                UUID           NOT NULL,
    first_name          VARCHAR(255)   NOT NULL,
    last_name           VARCHAR(255)   NOT NULL,
    email               VARCHAR(50)    NOT NULL,
    phone               VARCHAR(50)    NOT NULL,
    guests_count        INT            NOT NULL,
    reservation_status  VARCHAR(50)    NULL,
    accommodation_coast DECIMAL(19, 2) NOT NULL,
    start_date          DATE           NOT NULL,
    end_date            DATE           NOT NULL,
    is_deleted          TINYINT(1)     NOT NULL DEFAULT 0,
    created_date_time   DATETIME(6)    NOT NULL,
    created_by          UUID           NULL,
    updated_date_time   DATETIME(6)    NOT NULL,
    updated_by          UUID           NULL,

    CONSTRAINT pk_reservations PRIMARY KEY (uuid),

    CONSTRAINT fk_reservations_created_by
        FOREIGN KEY (created_by) REFERENCES users (uuid),

    CONSTRAINT fk_reservations_updated_by
        FOREIGN KEY (updated_by) REFERENCES users (uuid)
);

CREATE INDEX idx_reservations_created_by ON reservations (created_by);
CREATE INDEX idx_reservations_updated_by ON reservations (updated_by);

-- RESERVATIONS_ROOM_TYPES
CREATE TABLE reservations_room_types
(
    reservation_id UUID NOT NULL,
    room_type_id   UUID NOT NULL,
    rooms_count    INT  NOT NULL,

    CONSTRAINT pk_reservations_room_types
        PRIMARY KEY (reservation_id, room_type_id),

    CONSTRAINT fk_res_room_types_reservation
        FOREIGN KEY (reservation_id) REFERENCES reservations (uuid)
            ON DELETE CASCADE,

    CONSTRAINT fk_res_room_types_room_type
        FOREIGN KEY (room_type_id) REFERENCES room_types (uuid)
            ON DELETE RESTRICT,

    CONSTRAINT ck_res_room_types_rooms_count_positive
        CHECK (rooms_count > 0)
);

CREATE INDEX idx_res_room_types_reservation ON reservations_room_types (reservation_id);
CREATE INDEX idx_res_room_types_room_type ON reservations_room_types (room_type_id);

-- RESERVATIONS_ROOMS
CREATE TABLE reservations_rooms
(
    reservation_id UUID NOT NULL,
    room_id        UUID NOT NULL,

    CONSTRAINT pk_reservations_rooms
        PRIMARY KEY (reservation_id, room_id),

    CONSTRAINT fk_res_rooms_reservation
        FOREIGN KEY (reservation_id) REFERENCES reservations (uuid)
            ON DELETE CASCADE,

    CONSTRAINT fk_res_rooms_room
        FOREIGN KEY (room_id) REFERENCES rooms (uuid)
            ON DELETE RESTRICT
);

CREATE INDEX idx_res_rooms_reservation ON reservations_rooms (reservation_id);
CREATE INDEX idx_res_rooms_room ON reservations_rooms (room_id);