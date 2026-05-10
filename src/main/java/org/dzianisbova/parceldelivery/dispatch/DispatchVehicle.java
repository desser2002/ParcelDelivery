package org.dzianisbova.parceldelivery.dispatch;

import lombok.Getter;
import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Vehicle;

import java.util.UUID;

public class DispatchVehicle {
    @Getter
    private final Vehicle vehicle;
    private final double packedVolume;

    public DispatchVehicle(Vehicle vehicle, double packedVolume) {
        this.vehicle = vehicle;
        this.packedVolume = packedVolume;
    }

    UUID id() {
        return UUID.fromString(vehicle.getId());
    }

    Dimensions dimensions() {
        return vehicle.getDimensions();
    }

    double maxWeight() {
        return vehicle.getMaxWeight();
    }

    public double packedVolume() {
        return packedVolume;
    }

    double fillRatio() {
        return packedVolume / vehicle.getDimensions().volume();
    }

    String plateNumber() {
        return vehicle.getPlateNumber();
    }

    boolean isLoadComplete(double threshold) {
        return fillRatio() >= threshold;
    }

    DispatchVehicle withAddedVolume(double addedVolume) {
        return new DispatchVehicle(vehicle, this.packedVolume + addedVolume);
    }
}
