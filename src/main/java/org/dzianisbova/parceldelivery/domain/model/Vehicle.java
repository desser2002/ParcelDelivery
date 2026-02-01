package org.dzianisbova.parceldelivery.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = "id")
public class Vehicle {
    private final String id;
    private final Dimensions dimensions;
    private final double maxWeight;

    public Vehicle(String id, Dimensions dimensions, double maxWeight) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Vehicle id cannot be empty");
        }
        if (maxWeight <= 0) {
            throw new IllegalArgumentException("Max weight must be positive");
        }

        if (dimensions == null) {
            throw new IllegalArgumentException("Dimensions can't be null");
        }

        this.id = id;
        this.dimensions = dimensions;
        this.maxWeight = maxWeight;
    }
}
