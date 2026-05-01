package org.dzianisbova.parceldelivery.dispatch;

import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Vehicle;

import java.util.UUID;

public class DispatchVehicle {
    private final Vehicle vehicle;
    private final double packedVolume;

    public DispatchVehicle(Vehicle vehicle, double packedVolume) {
        this.vehicle = vehicle;
        this.packedVolume = packedVolume;
    }

    UUID id() {
        return vehicle.id();
    }

    Dimensions dimensions() {
        return vehicle.dimensions();
    }

    double maxWeight() {
        return vehicle.maxWeight();
    }

    public double packedVolume() {
        return packedVolume;
    }

    double fillRatio() {
        return packedVolume / vehicle.dimensions().volume();
    }

    String plateNumber() {
        return vehicle.plateNumber();
    }

    boolean isLoadComplete(double threshold) {
        return fillRatio() >= threshold;
    }

    DispatchVehicle withAddedVolume(double addedVolume) {
        return new DispatchVehicle(vehicle, this.packedVolume + addedVolume);
    }
}
