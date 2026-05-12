-- liquibase formatted sql
-- changeset dzianis:008-create-tracking-events

CREATE TABLE tracking_events(
    id UUID PRIMARY KEY,
    shipment_id UUID NOT NULL,
    type VARCHAR(20) NOT NULL,
    occurred_at TIMESTAMP NOT NULL,
    actor_id UUID,
    vehicle_id UUID,
    payload JSONB,

CONSTRAINT  fk_tracking_events_shipment
    FOREIGN KEY (shipment_id) REFERENCES shipments(id),

CONSTRAINT  fk_tracking_events_vehicle
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id)
);