package org.dzianisbova.parceldelivery.packing.domain.algorithm;

import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.packing.domain.service.PackingContext;
import org.dzianisbova.parceldelivery.packing.domain.model.Position;

public interface PackingAlgorithm {
    void initialize(PackingContext context);

    Position findPosition(Parcel parcel, PackingContext context);

    void notifyPlaced(Parcel parcel, Position position);
}
