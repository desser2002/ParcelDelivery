package org.dzianisbova.parceldelivery.dispatch.port;

import org.dzianisbova.parceldelivery.dispatch.DispatchVehicle;
import org.dzianisbova.parceldelivery.packing.domain.model.ParcelPlacement;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface DispatchVehicleRepository {
    List<DispatchVehicle> findAvailable();

    List<ParcelPlacement> findConfirmedPlacements(UUID vehicleId);

    void savePlacements(UUID vehicleId, List<ParcelPlacement> placements, Map<UUID, UUID> parcelIdToShipmentId);

    void deletePlacements(UUID vehicleId);
}
