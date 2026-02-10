package org.dzianisbova.parceldelivery.packing.domain.policy;

import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.packing.domain.service.PackingContext;
import org.dzianisbova.parceldelivery.packing.domain.model.Position;

import java.util.ArrayList;
import java.util.List;

public class PriorityPolicy implements PackingPolicy {
    @Override
    public boolean canPlace(Parcel parcel, Position position, PackingContext context) {
        return true;
    }

    @Override
    public List<Parcel> prepareParcels(List<Parcel> parcels) {
        List<Parcel> priority = new ArrayList<>();
        List<Parcel> normal = new ArrayList<>();

        for (Parcel parcel : parcels) {
            if (parcel.isPriority()) {
                priority.add(parcel);
            } else {
                normal.add(parcel);
            }
        }

        List<Parcel> result = new ArrayList<>(priority);
        result.addAll(normal);

        return result;
    }
}
