package org.dzianisbova.parceldelivery.packing.algorithm;

import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.packing.model.ParcelPlacement;
import org.dzianisbova.parceldelivery.packing.model.Position;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private final double startHeight;
    private final Dimensions containerBounds;
    private final List<ParcelPlacement> placements;
    private final List<Position> extremePoints;
    private double height;

    public Level(double startHeight, Dimensions containerBounds) {
        this.startHeight = startHeight;
        this.containerBounds = containerBounds;
        this.placements = new ArrayList<>();
        this.extremePoints = new ArrayList<>();
        this.extremePoints.add(new Position(0, 0, startHeight));
        this.height = 0;
    }

    public boolean canFit(Parcel parcel) {
        return findLowestAvailablePosition(parcel) != null;
    }

    private Position findLowestAvailablePosition(Parcel parcel) {
        Dimensions parcelDimensions = parcel.getDimensions();
        Position lowestPos = null;
        double minHeight = Double.MAX_VALUE;

        for (Position pos : extremePoints) {
            if (canPlaceAt(pos, parcelDimensions)) {
                double posHeight = pos.z();
                if (posHeight < minHeight) {
                    minHeight = posHeight;
                    lowestPos = pos;
                }
            }
        }
        return lowestPos;
    }

    private boolean canPlaceAt(Position pos, Dimensions parcelDimensions) {
        if (pos.x() + parcelDimensions.length() > containerBounds.length() ||
                pos.y() + parcelDimensions.width() > containerBounds.width() ||
                pos.z() + parcelDimensions.height() > containerBounds.height()) {
            return false;
        }
        for (ParcelPlacement placement : placements) {
            if (placement.collidesWith(pos, parcelDimensions)) {
                return false;
            }
        }
        return true;
    }

    public double getTopZ() {
        return startHeight + height;
    }
}
