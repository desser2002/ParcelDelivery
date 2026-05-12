package org.dzianisbova.parceldelivery.vehicle.domain.port;

import org.dzianisbova.parceldelivery.domain.model.Vehicle;

import java.util.UUID;

public interface VehicleRepository {
    Vehicle save(Vehicle vehicle);

    void assign(UUID vehicleId);

    void addPackedVolume(UUID vehicleId, double addedVolume);

    void deleteAll();
}
