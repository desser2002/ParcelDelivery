package org.dzianisbova.parceldelivery.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = "id")
public class Parcel {
    private final String id;
    private final Dimensions dimensions;
    private final double weight;

    public Parcel(String id, Dimensions dimensions, double weight) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Package id cannot be empty");
        }
        if (weight < 0) {
            throw new IllegalArgumentException("Weight cannot be negative");
        }
        this.id = id;
        this.dimensions = dimensions;
        this.weight = weight;
    }

    public double getBaseArea() {
        return dimensions.baseArea();
    }
}
