package org.dzianisbova.parceldelivery.packing.domain.algorithm;

import lombok.Getter;
import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.packing.domain.model.ParcelPlacement;
import org.dzianisbova.parceldelivery.packing.domain.model.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Level {
    @Getter private final double startHeight;
    private final Dimensions containerBounds;
    private final List<ParcelPlacement> placements;
    @Getter private double maxPlacedHeight;
    private final ExtremePointTracker tracker;

    public Level(double startHeight, Dimensions containerBounds) {
        this.startHeight = startHeight;
        this.containerBounds = containerBounds;
        this.placements = new ArrayList<>();
        this.maxPlacedHeight = 0;
        this.tracker = new ExtremePointTracker(startHeight, containerBounds);
    }

    public Position findLowestAvailablePosition(Parcel parcel) {
        return findLowestAvailablePosition(parcel, List.of());
    }

    public Position findLowestAvailablePosition(Parcel parcel, List<ParcelPlacement> contextPlacements) {
        Dimensions parcelDimensions = parcel.getDimensions();
        Position lowestPos = null;
        double minHeight = Double.MAX_VALUE;

        for (Position pos : tracker.getPoints()) {
            if (canPlaceAt(pos, parcelDimensions, contextPlacements)) {
                double posHeight = pos.z();
                if (posHeight < minHeight) {
                    minHeight = posHeight;
                    lowestPos = pos;
                }
            }
        }
        return lowestPos;
    }

    private boolean canPlaceAt(Position pos, Dimensions parcelDimensions, List<ParcelPlacement> contextPlacements) {
        if (exceedsBounds(pos, parcelDimensions)) {
            return false;
        }
        for (ParcelPlacement placement : placements) {
            if (placement.collidesWith(pos, parcelDimensions)) {
                return false;
            }
        }
        for (ParcelPlacement placement : contextPlacements) {
            if (placement.collidesWith(pos, parcelDimensions)) {
                return false;
            }
        }
        return true;
    }

    private boolean exceedsBounds(Position pos, Dimensions dim) {
        return pos.x() + dim.length() > containerBounds.length()
                || pos.y() + dim.width() > containerBounds.width()
                || pos.z() + dim.height() > containerBounds.height();
    }

    public double getTopZ() {
        return startHeight + maxPlacedHeight;
    }

    public List<ParcelPlacement> getPlacements() {
        return Collections.unmodifiableList(placements);
    }

    public void placeParcelAt(Parcel parcel, Position position) {
        if (!canPlaceAt(position, parcel.getDimensions(), List.of())) {
            throw new IllegalStateException("Cannot place parcel at position " + position);
        }

        ParcelPlacement placement = new ParcelPlacement(parcel, position);
        placements.add(placement);
        maxPlacedHeight = Math.max(maxPlacedHeight, parcel.getDimensions().height());
        tracker.update(placement);
    }
}
