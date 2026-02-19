package org.dzianisbova.parceldelivery.packing.domain.algorithm;

import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.packing.domain.service.PackingContext;
import org.dzianisbova.parceldelivery.packing.domain.model.Position;

import java.util.ArrayList;
import java.util.List;

public class LAFFLevelAlgorithm implements PackingAlgorithm {
    private List<Level> levels;
    private Dimensions vehicleDimensions;

    @Override
    public void initialize(PackingContext context) {
        this.vehicleDimensions = context.getVehicle().getDimensions();
        this.levels = new ArrayList<>();
        this.levels.add(new Level(0, vehicleDimensions));
    }

    @Override
    public Position findPosition(Parcel parcel, PackingContext context) {
        for (Level level : levels) {
            Position position = level.findLowestAvailablePosition(parcel, context.getPlacedParcels());
            if (position != null) {
                return position;
            }
        }
        Level lastLevel = levels.getLast();
        if (canCreateNewLevel(lastLevel, parcel)) {
            Level newLevel = new Level(lastLevel.getTopZ(), vehicleDimensions);
            levels.add(newLevel);
            return newLevel.findLowestAvailablePosition(parcel, context.getPlacedParcels());
        }

        return null;
    }

    @Override
    public void notifyPlaced(Parcel parcel, Position position) {
        for (int i = 0; i < levels.size(); i++) {
            Level level = levels.get(i);

            double levelStart = level.getStartHeight();
            double levelEnd = (i < levels.size() - 1)
                ? levels.get(i + 1).getStartHeight()
                : vehicleDimensions.height();

            if (position.z() >= levelStart && position.z() < levelEnd) {
                level.placeParcelAt(parcel, position);
                break;
            }
        }
    }

    private boolean canCreateNewLevel(Level lastLevel, Parcel parcel) {
        double newLevelStartHeight = lastLevel.getTopZ();
        double requiredHeight = newLevelStartHeight + parcel.getDimensions().height();
        return requiredHeight <= vehicleDimensions.height();
    }
}
