--liquibase formatted sql

--changeset dzianis:007-create-vehicle-packing-placements
CREATE TABLE vehicle_packing_placements
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    vehicle_id  UUID             NOT NULL REFERENCES vehicles (id),
    shipment_id UUID             NOT NULL REFERENCES shipments (id),
    pos_x       DOUBLE PRECISION NOT NULL,
    pos_y       DOUBLE PRECISION NOT NULL,
    pos_z       DOUBLE PRECISION NOT NULL,
    UNIQUE (vehicle_id, shipment_id)
);
