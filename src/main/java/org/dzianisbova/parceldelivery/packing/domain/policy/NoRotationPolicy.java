package org.dzianisbova.parceldelivery.packing.domain.policy;

import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.packing.domain.service.PackingContext;
import org.dzianisbova.parceldelivery.packing.domain.model.Position;

import java.util.List;

public class NoRotationPolicy implements PackingPolicy {
    @Override
    public boolean canPlace(Parcel parcel, Position position, PackingContext context) {
        return true;
    }

    @Override
    public List<Parcel> prepareParcels(List<Parcel> parcels) {
        return parcels;
    }
}
