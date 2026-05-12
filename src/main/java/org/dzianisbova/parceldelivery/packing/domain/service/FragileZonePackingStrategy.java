package org.dzianisbova.parceldelivery.packing.domain.service;

import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.domain.model.Vehicle;
import org.dzianisbova.parceldelivery.packing.domain.algorithm.PackingAlgorithm;
import org.dzianisbova.parceldelivery.packing.domain.algorithm.sort.ParcelSorter;
import org.dzianisbova.parceldelivery.packing.domain.model.ParcelPlacement;
import org.dzianisbova.parceldelivery.packing.domain.model.Position;
import org.dzianisbova.parceldelivery.packing.domain.model.VehiclePackingResult;
import org.dzianisbova.parceldelivery.packing.domain.policy.PackingPolicy;

import java.util.ArrayList;
import java.util.List;

class FragileZonePackingStrategy implements PackingStrategy {
    private final PackingAlgorithm algorithm;
    private final PackingAlgorithm fragileAlgorithm;
    private final ParcelSorter sorter;
    private final List<PackingPolicy> policies;

    public FragileZonePackingStrategy(PackingAlgorithm algorithm,
                                      PackingAlgorithm fragileAlgorithm,
                                      ParcelSorter sorter,
                                      List<PackingPolicy> policies) {
        if (algorithm == null) {
            throw new IllegalArgumentException("Algorithm cannot be null");
        }
        if (fragileAlgorithm == null) {
            throw new IllegalArgumentException("FragileAlgorithm cannot be null");
        }
        if (sorter == null) {
            throw new IllegalArgumentException("Sorter cannot be null");
        }
        this.algorithm = algorithm;
        this.fragileAlgorithm = fragileAlgorithm;
        this.sorter = sorter;
        this.policies = policies != null ? new ArrayList<>(policies) : new ArrayList<>();
    }

    @Override
    public VehiclePackingResult pack(List<Parcel> parcels, Vehicle vehicle,
                                     List<ParcelPlacement> existingPlacements) {
        List<Parcel> sorted = sorter.sort(parcels);

        PackingContext context = new PackingContext(vehicle, existingPlacements);
        algorithm.initialize(context);
        fragileAlgorithm.initialize(context);
        for (ParcelPlacement existing : existingPlacements) {
            PackingAlgorithm active = existing.parcel().isFragile() ? fragileAlgorithm : algorithm;
            active.notifyPlaced(existing.parcel(), existing.position());
        }
        List<ParcelPlacement> newPlacements = new ArrayList<>();
        for (Parcel parcel : sorted) {
            PackingAlgorithm active = parcel.isFragile() ? fragileAlgorithm : algorithm;

            Position position = null;
            if (context.exceedsWeightLimit(parcel.getWeight())) {
                position = active.findPosition(parcel, context);
            }

            if (position == null || policiesReject(parcel, position, context)) {
                continue;
            }

            ParcelPlacement placement = new ParcelPlacement(parcel, position);
            context.addPlacement(placement);
            active.notifyPlaced(parcel, position);
            newPlacements.add(placement);
        }

        return new VehiclePackingResult(vehicle.getId(), newPlacements);
    }

    private boolean policiesReject(Parcel parcel, Position position, PackingContext context) {
        for (PackingPolicy policy : policies) {
            if (!policy.canPlace(parcel, position, context)) {
                return true;
            }
        }
        return false;
    }
}
