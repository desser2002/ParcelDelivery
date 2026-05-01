package org.dzianisbova.parceldelivery.vehicle.domain.port;

import org.dzianisbova.parceldelivery.domain.model.Vehicle;

import java.util.List;
import java.util.UUID;

public interface VehicleRepository {
    List<Vehicle> findAvailable();

    void assign(UUID vehicleId);

    void addPackedVolume(UUID vehicleId, double addedVolume);
}
