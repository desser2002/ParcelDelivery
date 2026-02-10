package org.dzianisbova.parceldelivery.packing.domain.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VehiclePackingResult {
    @Getter
    private final String vehicleId;
    private final List<ParcelPlacement> placements;

    public VehiclePackingResult(String vehicleId, List<ParcelPlacement> placements) {
        if (vehicleId == null || vehicleId.isBlank()) {
            throw new IllegalArgumentException("Vehicle id cannot be empty");
        }
        this.vehicleId = vehicleId;
        this.placements = new ArrayList<>(placements);
    }

    public List<ParcelPlacement> getPlacements() {
        return Collections.unmodifiableList(placements);
    }
}
