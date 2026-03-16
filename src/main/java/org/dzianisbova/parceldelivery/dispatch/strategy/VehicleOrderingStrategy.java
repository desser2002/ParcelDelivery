package org.dzianisbova.parceldelivery.dispatch.strategy;

import org.dzianisbova.parceldelivery.dispatch.DispatchVehicle;

import java.util.List;

public interface VehicleOrderingStrategy {
    List<DispatchVehicle> sort(List<DispatchVehicle> vehicles);
}
