package org.dzianisbova.parceldelivery.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = "id")
public class Parcel {
    private final String id;
    private final Dimensions dimensions;
    private final double weight;
    private final boolean fragile;
    private final Priority priority;

    public Parcel(String id, Dimensions dimensions, double weight, boolean fragile, Priority priority) {
        this.fragile = fragile;
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Package id cannot be empty");
        }
        if (weight < 0) {
            throw new IllegalArgumentException("Weight cannot be negative");
        }
        if (priority == null) {
            throw new IllegalArgumentException("Priority cannot be null");
        }
        this.id = id;
        this.dimensions = dimensions;
        this.weight = weight;
        this.priority = priority;
    }

    public Parcel(String id, Dimensions dimensions, double weight) {
        this(id, dimensions, weight, false, Priority.STANDARD);
    }

    public boolean isPriority() {
        return priority == Priority.HIGH;
    }

    public boolean canRotate() {
        return true;
    }

    public double getBaseArea() {
        return dimensions.baseArea();
    }
}
