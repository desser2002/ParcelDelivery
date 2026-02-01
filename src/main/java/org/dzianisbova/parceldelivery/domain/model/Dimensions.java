package org.dzianisbova.parceldelivery.domain.model;

public record Dimensions(double length, double width, double height) {
    public Dimensions {
        if (length <= 0 || width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Dimensions must be positives");
        }
    }

    public double baseArea() {
        return length * width;
    }

    public double volume() {
        return length * width * height;
    }
}
