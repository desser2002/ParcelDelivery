package org.dzianisbova.parceldelivery.domain.model;

import java.util.UUID;

public record Vehicle(UUID id, String plateNumber, Dimensions dimensions, double maxWeight) {
    public Vehicle {
        if (id == null) {
            throw new IllegalArgumentException("Vehicle id cannot be null");
        }
        if (plateNumber == null || plateNumber.isBlank()) {
            throw new IllegalArgumentException("Vehicle plate number cannot be empty");
        }
        if (maxWeight <= 0) {
            throw new IllegalArgumentException("Max weight must be positive");
        }
        if (dimensions == null) {
            throw new IllegalArgumentException("Dimensions can't be null");
        }
    }
}
