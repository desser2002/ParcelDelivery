package org.dzianisbova.parceldelivery.packing.domain.service;

import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.domain.model.Vehicle;
import org.dzianisbova.parceldelivery.packing.domain.model.VehiclePackingResult;

import java.util.List;

public interface PackingStrategy {
    VehiclePackingResult pack(List<Parcel> parcels, Vehicle vehicle);
}
