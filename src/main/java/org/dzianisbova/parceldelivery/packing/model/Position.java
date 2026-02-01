package org.dzianisbova.parceldelivery.packing.model;

public record Position(double x, double y, double z) {
    public Position {
        if (x < 0 || y < 0 || z < 0) {
            throw new IllegalArgumentException("Position coordinates cannot be negative");
        }
    }

    public static Position origin() {
        return new Position(0, 0, 0);
    }
}
