package org.dzianisbova.parceldelivery.packing.domain.service;

import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.domain.model.Vehicle;
import org.dzianisbova.parceldelivery.packing.domain.algorithm.Level;
import org.dzianisbova.parceldelivery.packing.domain.algorithm.PackingAlgorithm;
import org.dzianisbova.parceldelivery.packing.domain.model.ParcelPlacement;
import org.dzianisbova.parceldelivery.packing.domain.model.Position;
import org.dzianisbova.parceldelivery.packing.domain.model.VehiclePackingResult;
import org.dzianisbova.parceldelivery.packing.domain.policy.PackingPolicy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PackingService {
    private final PackingAlgorithm algorithm;
    private final List<PackingPolicy> policies;

    public PackingService(PackingAlgorithm algorithm, List<PackingPolicy> policies) {
        if (algorithm == null) {
            throw new IllegalArgumentException("Algorithm cannot be null");
        }
        this.algorithm = algorithm;
        this.policies = policies != null ? new ArrayList<>(policies) : new ArrayList<>();
    }

    public VehiclePackingResult packWithFragileZone(List<Parcel> parcels, Vehicle vehicle) {
        List<Parcel> sorted = new ArrayList<>(parcels);
        sorted.sort(Comparator.comparingDouble(Parcel::getBaseArea).reversed());

        double maxFragileHeight = sorted.stream()
                .filter(Parcel::isFragile)
                .mapToDouble(p -> p.getDimensions().height())
                .max()
                .orElse(0);

        PackingContext context = new PackingContext(vehicle);
        algorithm.initialize(context);

        Level fragileLevel = null;
        double fragileLevelStart = 0;

        List<ParcelPlacement> placements = new ArrayList<>();

        for (Parcel parcel : sorted) {
            if (context.exceedsWeightLimit(parcel.getWeight())) {
                continue;
            }

            Position position;

            if (parcel.isFragile()) {
                if (fragileLevel == null) {
                    fragileLevelStart = vehicle.getDimensions().height() - maxFragileHeight;
                    fragileLevel = new Level(fragileLevelStart, vehicle.getDimensions());
                }

                position = fragileLevel.findLowestAvailablePosition(parcel);
                if (position != null && context.isSpaceOccupied(position, parcel.getDimensions())) {
                    position = null;
                }
                if (position == null) {
                    continue;
                }
            } else {
                position = algorithm.findPosition(parcel, context);

                if (position == null) {
                    continue;
                }

                if (fragileLevel != null) {
                    double parcelTop = position.z() + parcel.getDimensions().height();
                    if (parcelTop > fragileLevelStart) {
                        continue;
                    }
                }
            }

            if (policiesRejectPlacement(parcel, position, context)) {
                continue;
            }

            ParcelPlacement placement = new ParcelPlacement(parcel, position);
            placements.add(placement);
            context.addPlacement(placement);

            if (parcel.isFragile()) {
                if (fragileLevel != null) {
                    fragileLevel.placeParcelAt(parcel, position);
                } else {
                    throw new IllegalStateException("FragileLevel is null for fragile parcel: " + parcel.getId());
                }
            } else {
                algorithm.notifyPlaced(parcel, position);
            }
        }
        return new VehiclePackingResult(vehicle.getId(), placements);
    }

    private void packGroup(List<Parcel> parcels, PackingContext context,
                           List<ParcelPlacement> placements) {
        for (Parcel parcel : parcels) {
            if (context.exceedsWeightLimit(parcel.getWeight())) {
                continue;
            }

            Position position = algorithm.findPosition(parcel, context);
            if (position == null) {
                continue;
            }

            if (policiesRejectPlacement(parcel, position, context)) {
                continue;
            }

            ParcelPlacement placement = new ParcelPlacement(parcel, position);
            placements.add(placement);
            context.addPlacement(placement);

            algorithm.notifyPlaced(parcel, position);
        }
    }

    private List<Parcel> prepareParcels(List<Parcel> parcels) {
        List<Parcel> sorted = new ArrayList<>(parcels);
        sorted.sort(Comparator.comparingDouble(Parcel::getBaseArea).reversed());

        for (PackingPolicy policy : policies) {
            sorted = policy.prepareParcels(sorted);
        }
        return sorted;
    }

    private boolean policiesRejectPlacement(Parcel parcel, Position position,
                                            PackingContext context) {
        for (PackingPolicy policy : policies) {
            if (!policy.canPlace(parcel, position, context)) {
                return true;
            }
        }
        return false;
    }
}
