package org.dzianisbova.parceldelivery.packing.domain.algorithm.sort;

import org.dzianisbova.parceldelivery.domain.model.Parcel;

import java.util.List;

public interface ParcelSorter {
    List<Parcel> sort(List<Parcel> parcels);
}
