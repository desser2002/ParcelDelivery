package org.dzianisbova.parceldelivery.packing.domain.model;

import org.dzianisbova.parceldelivery.domain.model.Parcel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record MultiVehiclePackingResult(List<VehiclePackingResult> vehicleResults, List<Parcel> unpackedParcels) {
    public MultiVehiclePackingResult(
            List<VehiclePackingResult> vehicleResults,
            List<Parcel> unpackedParcels) {
        this.vehicleResults = new ArrayList<>(vehicleResults);
        this.unpackedParcels = new ArrayList<>(unpackedParcels);
    }

    @Override
    public List<VehiclePackingResult> vehicleResults() {
        return Collections.unmodifiableList(vehicleResults);
    }

    @Override
    public List<Parcel> unpackedParcels() {
        return Collections.unmodifiableList(unpackedParcels);
    }

    public int getTotalPackedParcels() {
        return vehicleResults.stream()
                .mapToInt(r -> r.placements().size())
                .sum();
    }

    public int getUsedVehicles() {
        return (int) vehicleResults.stream()
                .filter(r -> !r.placements().isEmpty())
                .count();
    }
}
