package org.dzianisbova.parceldelivery.packing.domain.model;

import org.dzianisbova.parceldelivery.domain.model.Parcel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiVehiclePackingResult {
    private final List<VehiclePackingResult> vehicleResults;
    private final List<Parcel> unpackedParcels;

    public MultiVehiclePackingResult(
            List<VehiclePackingResult> vehicleResults,
            List<Parcel> unpackedParcels) {
        this.vehicleResults = new ArrayList<>(vehicleResults);
        this.unpackedParcels = new ArrayList<>(unpackedParcels);
    }

    public List<VehiclePackingResult> getVehicleResults() {
        return Collections.unmodifiableList(vehicleResults);
    }

    public List<Parcel> getUnpackedParcels() {
        return Collections.unmodifiableList(unpackedParcels);
    }

    public int getTotalPackedParcels() {
        return vehicleResults.stream()
                .mapToInt(r -> r.getPlacements().size())
                .sum();
    }

    public int getUsedVehicles() {
        return (int) vehicleResults.stream()
                .filter(r -> !r.getPlacements().isEmpty())
                .count();
    }
}
