package org.dzianisbova.parceldelivery.packing.algorithm;

import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.domain.model.Vehicle;
import org.dzianisbova.parceldelivery.packing.model.ParcelPlacement;
import org.dzianisbova.parceldelivery.packing.model.VehiclePackingResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Packing {
    public VehiclePackingResult pack(List<Parcel> parcels, Vehicle vehicle) {
        List<Parcel> sortedParcels = sortByBaseAreaDesc(parcels);
        List<ParcelPlacement> allPlacements = new ArrayList<>();
        Dimensions vehicleDimensions = vehicle.getDimensions();

        double currentWeight = 0;
        Level currentLevel = new Level(0, vehicleDimensions);

        for (Parcel parcel : sortedParcels) {
            if (currentWeight + parcel.getWeight() > vehicle.getMaxWeight()) {
                continue;
            }
            if (currentLevel.canFit(parcel)) {
                currentWeight = placeParcelAndUpdateWeight(parcel, currentLevel, allPlacements, currentWeight);
            } else if (canCreateNewLevel(currentLevel, parcel, vehicleDimensions)) {
                currentLevel = new Level(currentLevel.getTopZ(), vehicleDimensions);
                currentWeight = placeParcelAndUpdateWeight(parcel, currentLevel, allPlacements, currentWeight);
            }
        }
        return new VehiclePackingResult(vehicle.getId(), allPlacements);
    }

    private static double placeParcelAndUpdateWeight(Parcel parcel, Level currentLevel,
                                                     List<ParcelPlacement> allPlacements, double currentWeight) {
        ParcelPlacement placement = currentLevel.placeParcel(parcel);
        allPlacements.add(placement);
        currentWeight += parcel.getWeight();
        return currentWeight;
    }

    private List<Parcel> sortByBaseAreaDesc(List<Parcel> parcels) {
        return parcels.stream()
                .sorted(Comparator.comparingDouble(Parcel::getBaseArea).reversed())
                .toList();
    }

    private boolean canCreateNewLevel(Level currentLevel, Parcel parcel, Dimensions vehicleDimensions) {
        double newLevelStartHeight = currentLevel.getTopZ();
        double requiredHeight = newLevelStartHeight + parcel.getDimensions().height();
        return requiredHeight <= vehicleDimensions.height();
    }
}
