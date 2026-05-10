package org.dzianisbova.parceldelivery.domain.model;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Vehicle {
    private final String id;
    private final String plateNumber;
    private final Dimensions dimensions;
    private final double maxWeight;
    private final VehicleStatus status;
    private final double packedVolume;

    public Vehicle(String id, String plateNumber, Dimensions dimensions,
                   double maxWeight, VehicleStatus status, double packedVolume) {
        this.id = id;
        this.plateNumber = plateNumber;
        this.dimensions = dimensions;
        this.maxWeight = maxWeight;
        this.status = status;
        this.packedVolume = packedVolume;
        verify();
    }

    public Vehicle(String plateNumber, Dimensions dimensions, double maxWeight) {
        this(UUID.randomUUID().toString(), plateNumber, dimensions, maxWeight, VehicleStatus.AVAILABLE, 0);
    }

    private void verify() {
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
