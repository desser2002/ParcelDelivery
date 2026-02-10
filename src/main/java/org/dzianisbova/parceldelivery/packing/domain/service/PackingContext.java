package org.dzianisbova.parceldelivery.packing.domain.service;

import lombok.Getter;
import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Vehicle;
import org.dzianisbova.parceldelivery.packing.domain.model.ParcelPlacement;
import org.dzianisbova.parceldelivery.packing.domain.model.Position;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PackingContext {
    private final Vehicle vehicle;
    private final List<ParcelPlacement> placedParcels;
    private double currentWeight;

    public PackingContext(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.placedParcels = new ArrayList<>();
        this.currentWeight = 0.0;
    }

    public void addPlacement(ParcelPlacement placement) {
        placedParcels.add(placement);
        currentWeight += placement.getParcel().getWeight();
    }

    public List<ParcelPlacement> getParcelsAbove(Position position, Dimensions dimensions) {
        List<ParcelPlacement> above = new ArrayList<>();

        double topZ = position.z() + dimensions.height();
        double leftX = position.x();
        double rightX = position.x() + dimensions.length();
        double frontY = position.y();
        double backY = position.y() + dimensions.width();

        for (ParcelPlacement placed : placedParcels) {
            Position placedPos = placed.getPosition();
            Dimensions placedDim = placed.getParcel().getDimensions();

            if (placedPos.z() <= topZ) {
                continue;
            }

            boolean overlapX = placedPos.x() < rightX &&
                    (placedPos.x() + placedDim.length()) > leftX;

            boolean overlapY = placedPos.y() < backY &&
                    (placedPos.y() + placedDim.width()) > frontY;

            if (overlapX && overlapY) {
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
}
