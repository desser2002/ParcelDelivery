package org.dzianisbova.parceldelivery.packing.model;

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

        boolean collisionX = otherPosition.x() < position.x() + thisDimensions.length() &&
                otherPosition.x() + otherDimensions.length() > position.x();

        boolean collisionY = otherPosition.y() < position.y() + thisDimensions.width() &&
                otherPosition.y() + otherDimensions.width() > position.y();

        boolean collisionZ = otherPosition.z() < position.z() + thisDimensions.height() &&
                otherPosition.z() + otherDimensions.height() > position.z();

        return collisionX && collisionY && collisionZ;
    }
}
