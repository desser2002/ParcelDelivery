package org.dzianisbova.parceldelivery.vehicle.infrastructure.web;

import org.dzianisbova.parceldelivery.domain.model.Vehicle;

import java.util.UUID;

public record VehicleResponse(UUID id, String plateNumber, DimensionsDto dimensionsDto, double maxWeight,
                              String status, double packedVolume) {
    static VehicleResponse from(Vehicle vehicle) {
        return new VehicleResponse(vehicle.getId(),
            vehicle.getPlateNumber(),
            DimensionsDto.from(vehicle.getDimensions()),
            vehicle.getMaxWeight(),
            vehicle.getStatus().toString(),
            vehicle.getPackedVolume());
    }
}
