-- liquibase formatted sql

-- changeset dzianis:001-create-vehicles-table
CREATE TABLE vehicles
(
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    plate_number VARCHAR(20)      NOT NULL UNIQUE,
    length       DOUBLE PRECISION NOT NULL,
    width        DOUBLE PRECISION NOT NULL,
    height       DOUBLE PRECISION NOT NULL,
    max_weight   DOUBLE PRECISION NOT NULL
);

-- changeset dzianis:001-create-shipments-table
CREATE TABLE shipments
(
    id               UUID PRIMARY KEY          DEFAULT gen_random_uuid(),
    tracking_number  VARCHAR(20)      NOT NULL UNIQUE,
    sender_id        UUID,

    pickup_street    VARCHAR(255)     NOT NULL,
    pickup_building  VARCHAR(20)      NOT NULL,
    pickup_apartment VARCHAR(20),
    pickup_city      VARCHAR(100)     NOT NULL,
    pickup_postal_code VARCHAR(20),
    pickup_country       VARCHAR(2)       NOT NULL,

    recipient_name       VARCHAR(255)     NOT NULL,

    delivery_street      VARCHAR(255)   NOT NULL,
    delivery_building    VARCHAR(20)    NOT NULL,
    delivery_apartment   VARCHAR(20),
    delivery_city        VARCHAR(100)   NOT NULL,
    delivery_postal_code VARCHAR(20),
    delivery_country     VARCHAR(2)        NOT NULL,

    length           DOUBLE PRECISION NOT NULL,
    width            DOUBLE PRECISION NOT NULL,
    height           DOUBLE PRECISION NOT NULL,
    weight           DOUBLE PRECISION NOT NULL,
    fragile          BOOLEAN          NOT NULL DEFAULT false,

    priority         VARCHAR(20)      NOT NULL DEFAULT 'STANDARD',
    status           VARCHAR(20)      NOT NULL DEFAULT 'PENDING',
    created_at       TIMESTAMP        NOT NULL DEFAULT now()
);




