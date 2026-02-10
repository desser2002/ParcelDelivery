package org.dzianisbova.parceldelivery.packing.domain.policy;

import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.packing.domain.service.PackingContext;
import org.dzianisbova.parceldelivery.packing.domain.model.ParcelPlacement;
import org.dzianisbova.parceldelivery.packing.domain.model.Position;

import java.util.ArrayList;
import java.util.List;

public class FragilePolicy implements PackingPolicy {
    @Override
    public boolean canPlace(Parcel parcel, Position position, PackingContext context) {
        if (!parcel.isFragile()) {
            return true;
        }

        List<ParcelPlacement> parcelsAbove = context.getParcelsAbove(
                position,
                parcel.getDimensions()
        );

        return parcelsAbove.isEmpty();
    }

    @Override
    public List<Parcel> prepareParcels(List<Parcel> parcels) {
        List<Parcel> nonFragile = new ArrayList<>();
        List<Parcel> fragile = new ArrayList<>();

        for (Parcel parcel : parcels) {
            if (parcel.isFragile()) {
                fragile.add(parcel);
            } else {
                nonFragile.add(parcel);
            }
        }

        List<Parcel> result = new ArrayList<>(nonFragile);
        result.addAll(fragile);

        return result;
    }
}
