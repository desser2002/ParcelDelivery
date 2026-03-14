-- liquibase formatted sql

-- changeset dzianis:004-alter-vehicles-add-status
ALTER TABLE vehicles ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE';
