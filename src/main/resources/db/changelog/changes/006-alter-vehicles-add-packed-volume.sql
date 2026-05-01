-- liquibase formatted sql
-- changeset dzianis:006-alter-vehicles-add-packed-volume
ALTER TABLE vehicles ADD COLUMN packed_volume DOUBLE PRECISION NOT NULL DEFAULT 0;