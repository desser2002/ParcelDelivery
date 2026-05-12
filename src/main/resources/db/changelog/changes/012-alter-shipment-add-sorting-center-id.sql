-- liquibase formatted sql
-- changeset dzianis:012-alter-shipment-add-sorting-center-id.sql

ALTER TABLE shipments
    ADD COLUMN sorting_center_id UUID REFERENCES sorting_centers (id);