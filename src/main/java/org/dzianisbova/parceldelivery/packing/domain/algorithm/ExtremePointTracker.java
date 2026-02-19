package org.dzianisbova.parceldelivery.packing.domain.algorithm;

import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.packing.domain.model.ParcelPlacement;
import org.dzianisbova.parceldelivery.packing.domain.model.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExtremePointTracker {
    private final List<Position> points;
    private final Dimensions containerBounds;

    public ExtremePointTracker(double startHeight, Dimensions containerBounds) {
        this.containerBounds = containerBounds;
        this.points = new ArrayList<>();
        this.points.add(new Position(0, 0, startHeight));
    }

    public List<Position> getPoints() {
        return Collections.unmodifiableList(points);
    }

    public void update(ParcelPlacement placement) {
        Position position = placement.getPosition();
        Dimensions dimensions = placement.getParcel().getDimensions();
        points.remove(position);
        List<Position> newPoints = generateNew(position, dimensions);
        points.removeIf(point -> isInsideParcel(point, placement));
        points.addAll(newPoints);
    }

    private List<Position> generateNew(Position pos, Dimensions dim) {
        List<Position> newPoints = new ArrayList<>();

        Position extremeX = new Position(pos.x() + dim.length(), pos.y(), pos.z());
        if (extremeX.x() < containerBounds.length()) {
            newPoints.add(extremeX);
        }

        Position extremeY = new Position(pos.x(), pos.y() + dim.width(), pos.z());
        if (extremeY.y() < containerBounds.width()) {
            newPoints.add(extremeY);
        }

        return newPoints;
    }

    private boolean isInsideParcel(Position point, ParcelPlacement placement) {
        Position pos = placement.getPosition();
        Dimensions dim = placement.getParcel().getDimensions();

        boolean insideX = point.x() >= pos.x() && point.x() < pos.x() + dim.length();
        boolean insideY = point.y() >= pos.y() && point.y() < pos.y() + dim.width();
        boolean insideZ = point.z() >= pos.z() && point.z() < pos.z() + dim.height();

        return insideX && insideY && insideZ;
    }
}
