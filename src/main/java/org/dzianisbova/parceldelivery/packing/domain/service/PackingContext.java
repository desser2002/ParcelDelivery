package org.dzianisbova.parceldelivery.packing.domain.service;

import lombok.Getter;
import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Vehicle;
import org.dzianisbova.parceldelivery.packing.domain.model.ParcelPlacement;
import org.dzianisbova.parceldelivery.packing.domain.model.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PackingContext {
    @Getter private final Vehicle vehicle;
    private final List<ParcelPlacement> placedParcels;
    @Getter private double currentWeight;

    public PackingContext(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.placedParcels = new ArrayList<>();
        this.currentWeight = 0.0;
    }

    public List<ParcelPlacement> getPlacedParcels() {
        return Collections.unmodifiableList(placedParcels);
    }

    public void addPlacement(ParcelPlacement placement) {
        placedParcels.add(placement);
        currentWeight += placement.getParcel().getWeight();
    }

    public List<ParcelPlacement> getFragileParcelsBelow(Position position, Dimensions dimensions) {
        List<ParcelPlacement> below = new ArrayList<>();

        double bottomZ = position.z();

        for (ParcelPlacement placed : placedParcels) {
            if (!placed.getParcel().isFragile()) {
                continue;
            }

            Position placedPos = placed.getPosition();
            Dimensions placedDim = placed.getParcel().getDimensions();

            if (placedPos.z() + placedDim.height() > bottomZ) {
                continue;
            }

            if (hasXYOverlap(placedPos, placedDim, position, dimensions)) {
                below.add(placed);
            }
        }
        return below;
    }

    public List<ParcelPlacement> getParcelsAbove(Position position, Dimensions dimensions) {
        List<ParcelPlacement> above = new ArrayList<>();

        double topZ = position.z() + dimensions.height();

        for (ParcelPlacement placed : placedParcels) {
            Position placedPos = placed.getPosition();
            Dimensions placedDim = placed.getParcel().getDimensions();

            if (placedPos.z() < topZ) {
                continue;
            }

            if (hasXYOverlap(placedPos, placedDim, position, dimensions)) {
                above.add(placed);
            }
        }
        return above;
    }

    public boolean exceedsWeightLimit(double weight) {
        return currentWeight + weight > vehicle.getMaxWeight();
    }

    public boolean isSpaceOccupied(Position position, Dimensions dimensions) {
        for (ParcelPlacement placed : placedParcels) {
            if (placed.collidesWith(position, dimensions)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasXYOverlap(Position placedPos, Dimensions placedDim,
                                  Position targetPos, Dimensions targetDim) {
        boolean overlapX = placedPos.x() < targetPos.x() + targetDim.length() &&
                placedPos.x() + placedDim.length() > targetPos.x();

        boolean overlapY = placedPos.y() < targetPos.y() + targetDim.width() &&
                placedPos.y() + placedDim.width() > targetPos.y();

        return overlapX && overlapY;
    }
}
