package org.dzianisbova.parceldelivery.packing.domain.algorithm.sort;

import org.dzianisbova.parceldelivery.domain.model.Parcel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BaseAreaDescSorter implements ParcelSorter {
    @Override
    public List<Parcel> sort(List<Parcel> parcels) {
        List<Parcel> sorted = new ArrayList<>(parcels);
        sorted.sort(Comparator.comparingDouble(Parcel::getBaseArea).reversed());
        return sorted;
    }
}
