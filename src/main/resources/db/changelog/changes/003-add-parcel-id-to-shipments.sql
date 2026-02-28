-- liquibase formatted sql

-- changeset dzianis:003-add-parcel-id-to-shipments
ALTER TABLE shipments
    ADD COLUMN parcel_id UUID NOT NULL DEFAULT gen_random_uuid();

ALTER TABLE shipments
    ALTER COLUMN parcel_id DROP DEFAULT;
