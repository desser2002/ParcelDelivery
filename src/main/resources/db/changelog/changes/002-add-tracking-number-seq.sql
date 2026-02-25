-- liquibase formatted sql

-- changeset dzianis:002-add-tracking-number-seq
CREATE SEQUENCE tracking_number_seq
    START WITH 1000000000
    INCREMENT BY 1
    NO CYCLE;