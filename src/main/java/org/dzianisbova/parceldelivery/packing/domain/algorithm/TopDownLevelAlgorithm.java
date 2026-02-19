package org.dzianisbova.parceldelivery.packing.domain.algorithm;

import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.packing.domain.model.Position;
import org.dzianisbova.parceldelivery.packing.domain.service.PackingContext;

import java.util.ArrayList;
import java.util.List;

public class TopDownLevelAlgorithm implements PackingAlgorithm {
    private final List<Level> levels = new ArrayList<>();
    private Dimensions vehicleDimensions;

    @Override
    public void initialize(PackingContext context) {
        this.vehicleDimensions = context.getVehicle().getDimensions();
        this.levels.clear();
    }

    @Override
    public Position findPosition(Parcel parcel, PackingContext context) {
        for (Level level : levels) {
            Position pos = level.findLowestAvailablePosition(parcel, context.getPlacedParcels());
            if (pos != null) {
                return pos;
            }
        }

        double newStartHeight = levels.isEmpty()
                ? vehicleDimensions.height() - parcel.getDimensions().height()
                : levels.getLast().getStartHeight() - parcel.getDimensions().height();

        if (newStartHeight < 0) {
            return null;
        }

        Level newLevel = new Level(newStartHeight, vehicleDimensions);
        levels.add(newLevel);
        return newLevel.findLowestAvailablePosition(parcel, context.getPlacedParcels());
    }

    @Override
    public void notifyPlaced(Parcel parcel, Position position) {
        for (int i = 0; i < levels.size(); i++) {
            Level level = levels.get(i);
            double levelStart = level.getStartHeight();
            double levelEnd = (i == 0)
                    ? vehicleDimensions.height()
                    : levels.get(i - 1).getStartHeight();

            if (position.z() >= levelStart && position.z() < levelEnd) {
                level.placeParcelAt(parcel, position);
                return;
            }
        }
    }
}
