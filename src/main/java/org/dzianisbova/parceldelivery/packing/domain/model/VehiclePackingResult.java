package org.dzianisbova.parceldelivery.packing.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public record VehiclePackingResult(UUID vehicleId, List<ParcelPlacement> placements) {
    public VehiclePackingResult(UUID vehicleId, List<ParcelPlacement> placements) {
        if (vehicleId == null) {
            throw new IllegalArgumentException("Vehicle id cannot be null");
        }
        this.vehicleId = vehicleId;
        this.placements = new ArrayList<>(placements);
    }

    @Override
    public List<ParcelPlacement> placements() {
        return Collections.unmodifiableList(placements);
    }
}
