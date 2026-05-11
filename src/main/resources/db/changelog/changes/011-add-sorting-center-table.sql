-- liquibase formatted sql
-- changeset dzianis:011-add-sorting-center-table.sql

CREATE TABLE sorting_centers
(
    id   UUID PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);