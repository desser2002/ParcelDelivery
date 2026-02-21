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

public class FragileZonePackingStrategy implements PackingStrategy {
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
    public VehiclePackingResult pack(List<Parcel> parcels, Vehicle vehicle) {
        List<Parcel> sorted = sorter.sort(parcels);

        PackingContext context = new PackingContext(vehicle);
        algorithm.initialize(context);
        fragileAlgorithm.initialize(context);

        List<ParcelPlacement> placements = new ArrayList<>();

        for (Parcel parcel : sorted) {
            if (context.exceedsWeightLimit(parcel.getWeight())) {
                continue;
            }

            PackingAlgorithm active = parcel.isFragile() ? fragileAlgorithm : algorithm;

            Position position = active.findPosition(parcel, context);
            if (position == null) {
                continue;
            }

            if (policiesReject(parcel, position, context)) {
                continue;
            }

            ParcelPlacement placement = new ParcelPlacement(parcel, position);
            context.addPlacement(placement);
            active.notifyPlaced(parcel, position);
            placements.add(placement);
        }

        return new VehiclePackingResult(vehicle.getId(), placements);
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
