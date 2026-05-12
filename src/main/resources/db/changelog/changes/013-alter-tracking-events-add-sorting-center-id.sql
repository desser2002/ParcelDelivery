-- liquibase formatted sql
-- changeset dzianis:013-alter-tracking-events-add-sorting-center-id

ALTER TABLE tracking_events
    ADD COLUMN sorting_center_id UUID REFERENCES sorting_centers (id);
