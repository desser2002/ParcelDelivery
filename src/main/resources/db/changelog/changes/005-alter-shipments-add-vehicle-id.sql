-- liquibase formatted sql

-- changeset dzianis:005-alter-shipments-add-vehicle-id
ALTER TABLE shipments ADD COLUMN vehicle_id UUID REFERENCES vehicles(id);
