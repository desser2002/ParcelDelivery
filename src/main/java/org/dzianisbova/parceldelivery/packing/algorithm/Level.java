package org.dzianisbova.parceldelivery.packing.algorithm;

import lombok.Getter;
import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.packing.model.ParcelPlacement;
import org.dzianisbova.parceldelivery.packing.model.Position;

import java.util.ArrayList;
import java.util.List;

@Getter
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

    public ParcelPlacement placeParcel(Parcel parcel) {
        Position position = findLowestAvailablePosition(parcel);
        if (position == null) {
            throw new IllegalStateException("Cannot place parcel - no available position found");
        }

        ParcelPlacement placement = new ParcelPlacement(parcel, position);
        placements.add(placement);

        height = Math.max(height, parcel.getDimensions().height());

        updateExtremePoints(placement);

        return placement;
    }

    private void updateExtremePoints(ParcelPlacement placement) {
        Position position = placement.getPosition();
        Dimensions dimensions = placement.getParcel().getDimensions();
        extremePoints.remove(position);
        List<Position> newPoints = generateNewExtremePoints(position, dimensions);
        extremePoints.removeIf(point -> isPointInsideParcel(point, placement));
        extremePoints.addAll(newPoints);
    }

    private boolean isPointInsideParcel(Position point, ParcelPlacement placement) {
        Position pos = placement.getPosition();
        Dimensions dim = placement.getParcel().getDimensions();

        return point.x() >= pos.x() && point.x() < pos.x() + dim.length() &&
                point.y() >= pos.y() && point.y() < pos.y() + dim.width() &&
                point.z() >= pos.z() && point.z() < pos.z() + dim.height();
    }

    private List<Position> generateNewExtremePoints(Position pos, Dimensions dim) {
        List<Position> newPoints = new ArrayList<>();

        Position extremeX = new Position(pos.x() + dim.length(), pos.y(), pos.z());
        if (extremeX.x() < containerBounds.length()) {
            newPoints.add(extremeX);
        }

        Position extremeY = new Position(pos.x(), pos.y() + dim.width(), pos.z());
        if (extremeY.y() < containerBounds.width()) {
            newPoints.add(extremeY);
        }

        Position extremeZ = new Position(pos.x(), pos.y(), pos.z() + dim.height());
        if (extremeZ.z() < containerBounds.height()) {
            newPoints.add(extremeZ);
        }

        return newPoints;
    }
}
