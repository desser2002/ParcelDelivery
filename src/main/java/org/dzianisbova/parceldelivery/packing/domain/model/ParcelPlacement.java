package org.dzianisbova.parceldelivery.packing.domain.model;

import lombok.Getter;
import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Parcel;

@Getter
public class ParcelPlacement {
    private final Parcel parcel;
    private final Position position;

    public ParcelPlacement(Parcel parcel, Position position) {
        if (parcel == null) {
            throw new IllegalArgumentException("Parcel cannot be null");
        }
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        this.parcel = parcel;
        this.position = position;
    }

    public boolean collidesWith(Position otherPosition, Dimensions otherDimensions) {
        Dimensions thisDimensions = parcel.getDimensions();

        boolean overlapX = otherPosition.x() < position.x() + thisDimensions.length() &&
                otherPosition.x() + otherDimensions.length() > position.x();

        boolean overlapY = otherPosition.y() < position.y() + thisDimensions.width() &&
                otherPosition.y() + otherDimensions.width() > position.y();

        boolean overlapZ = otherPosition.z() < position.z() + thisDimensions.height() &&
                otherPosition.z() + otherDimensions.height() > position.z();

        return overlapX && overlapY && overlapZ;
    }
}
