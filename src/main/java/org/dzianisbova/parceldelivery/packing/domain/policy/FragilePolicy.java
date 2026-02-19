package org.dzianisbova.parceldelivery.packing.domain.policy;

import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.packing.domain.model.Position;
import org.dzianisbova.parceldelivery.packing.domain.service.PackingContext;
import org.springframework.stereotype.Component;

@Component
public class FragilePolicy implements PackingPolicy {
    @Override
    public boolean canPlace(Parcel parcel, Position position, PackingContext context) {
        if (!context.getFragileParcelsBelow(position, parcel.getDimensions()).isEmpty()) {
            return false;
        }
        return !parcel.isFragile() || context.getParcelsAbove(position, parcel.getDimensions()).isEmpty();
    }
}
