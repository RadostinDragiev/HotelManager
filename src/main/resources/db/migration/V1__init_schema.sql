-- ROLES
CREATE TABLE roles
(
    uuid              UUID         NOT NULL,
    name              VARCHAR(255) NOT NULL UNIQUE,
    created_date_time TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (uuid)
);

-- USERS
CREATE TABLE users
(
    uuid                 UUID         NOT NULL,
    username             VARCHAR(255) NOT NULL,
    password             VARCHAR(255) NOT NULL,
    email                VARCHAR(255) NOT NULL,
    first_name           VARCHAR(255) NOT NULL,
    last_name            VARCHAR(255) NOT NULL,
    position             VARCHAR(255) NOT NULL,
    is_enabled           BIT          NOT NULL DEFAULT 1,
    created_by           UUID NULL,
    created_date_time    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login_date_time TIMESTAMP    NOT NULL,
    PRIMARY KEY (uuid),
    CONSTRAINT uk_users_username UNIQUE (username),
    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT fk_users_created_by
        FOREIGN KEY (created_by) REFERENCES users (uuid)
);

-- ROOMS
CREATE TABLE rooms
(
    uuid              UUID           NOT NULL,
    room_number       VARCHAR(50)    NOT NULL,
    room_type         VARCHAR(50) NULL,
    capacity          INT            NOT NULL,
    price_per_night   DECIMAL(19, 2) NOT NULL,
    description       TEXT NULL,
    room_status       VARCHAR(50) NULL,
    created_date_time TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by        UUID NULL,
    updated_date_time TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by        UUID NULL,
    PRIMARY KEY (uuid),
    CONSTRAINT uk_rooms_room_number UNIQUE (room_number),
    CONSTRAINT fk_rooms_created_by
        FOREIGN KEY (created_by) REFERENCES users (uuid),
    CONSTRAINT fk_rooms_updated_by
        FOREIGN KEY (updated_by) REFERENCES users (uuid)
);

-- USERS_ROLES
CREATE TABLE users_roles
(
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_users_roles_user
        FOREIGN KEY (user_id) REFERENCES users (uuid),
    CONSTRAINT fk_users_roles_role
        FOREIGN KEY (role_id) REFERENCES roles (uuid)
);

-- ROOM_BEDS
CREATE TABLE room_beds
(
    room_id   UUID        NOT NULL,
    bed_types VARCHAR(50) NOT NULL,
    PRIMARY KEY (room_id, bed_types),
    CONSTRAINT fk_room_beds_room
        FOREIGN KEY (room_id) REFERENCES rooms (uuid)
);
