package org.dzianisbova.parceldelivery.dispatch.port;

import org.dzianisbova.parceldelivery.dispatch.DispatchVehicle;

import java.util.List;

public interface DispatchVehicleRepository {
    List<DispatchVehicle> findAvailable();
}
