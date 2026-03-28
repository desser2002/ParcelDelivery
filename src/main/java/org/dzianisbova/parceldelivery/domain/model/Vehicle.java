package org.dzianisbova.parceldelivery.domain.model;

public record Vehicle(String id, Dimensions dimensions, double maxWeight) {
    public Vehicle {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Vehicle id cannot be empty");
        }
        if (maxWeight <= 0) {
            throw new IllegalArgumentException("Max weight must be positive");
        }

        if (dimensions == null) {
            throw new IllegalArgumentException("Dimensions can't be null");
        }
    }
}
