package org.dzianisbova.parceldelivery.packing.domain.service;

import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.domain.model.Vehicle;
import org.dzianisbova.parceldelivery.packing.domain.model.MultiVehiclePackingResult;
import org.dzianisbova.parceldelivery.packing.domain.model.VehiclePackingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MultiVehiclePackingService {
    private final PackingStrategy packingStrategy;

    public MultiVehiclePackingService(PackingStrategy packingStrategy) {
        if (packingStrategy == null) {
            throw new IllegalArgumentException("PackingStrategy cannot be null");
        }
        this.packingStrategy = packingStrategy;
    }

    public MultiVehiclePackingResult packSequentially(List<Parcel> parcels, List<Vehicle> vehicles) {
        validateInputs(parcels, vehicles);
        List<VehiclePackingResult> results = new ArrayList<>();
        List<Parcel> remaining = new ArrayList<>(parcels);
        for (Vehicle vehicle : vehicles) {
            if (remaining.isEmpty()) {
                break;
            }
            VehiclePackingResult result = packingStrategy.pack(remaining, vehicle);

            if (!result.getPlacements().isEmpty()) {
                results.add(result);
                Set<String> packedIds = result.getPlacements().stream()
                        .map(p -> p.getParcel().getId())
                        .collect(Collectors.toSet());

                remaining = remaining.stream()
                        .filter(p -> !packedIds.contains(p.getId()))
                        .collect(Collectors.toList());
            } else {
                break;
            }
        }
        return new MultiVehiclePackingResult(results, remaining);
    }

    private void validateInputs(List<Parcel> parcels, List<Vehicle> vehicles) {
        if (parcels == null || parcels.isEmpty()) {
            throw new IllegalArgumentException("Parcels list cannot be null or empty");
        }
        if (vehicles == null || vehicles.isEmpty()) {
            throw new IllegalArgumentException("Vehicles list cannot be null or empty");
        }
    }
}
