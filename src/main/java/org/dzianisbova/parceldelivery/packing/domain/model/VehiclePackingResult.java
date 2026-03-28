package org.dzianisbova.parceldelivery.packing.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record VehiclePackingResult(String vehicleId, List<ParcelPlacement> placements) {
    public VehiclePackingResult(String vehicleId, List<ParcelPlacement> placements) {
        if (vehicleId == null || vehicleId.isBlank()) {
            throw new IllegalArgumentException("Vehicle id cannot be empty");
        }
        this.vehicleId = vehicleId;
        this.placements = new ArrayList<>(placements);
    }

    @Override
    public List<ParcelPlacement> placements() {
        return Collections.unmodifiableList(placements);
    }
}
