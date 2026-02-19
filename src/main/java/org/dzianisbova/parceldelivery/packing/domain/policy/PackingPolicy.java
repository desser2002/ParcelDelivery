package org.dzianisbova.parceldelivery.packing.domain.policy;

import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.packing.domain.model.Position;
import org.dzianisbova.parceldelivery.packing.domain.service.PackingContext;

public interface PackingPolicy {
    boolean canPlace(Parcel parcel, Position position, PackingContext context);
}
