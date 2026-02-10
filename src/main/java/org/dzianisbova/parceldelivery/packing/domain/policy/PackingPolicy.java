package org.dzianisbova.parceldelivery.packing.domain.policy;

import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.packing.domain.service.PackingContext;
import org.dzianisbova.parceldelivery.packing.domain.model.Position;

import java.util.List;

public interface PackingPolicy {
    boolean canPlace(Parcel parcel, Position position, PackingContext context);

    List<Parcel> prepareParcels(List<Parcel> parcels);
}
