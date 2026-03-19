package org.dzianisbova.parceldelivery.dispatch.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.dzianisbova.parceldelivery.dispatch.DispatchVehicle;
import org.dzianisbova.parceldelivery.dispatch.port.DispatchVehicleRepository;
import org.dzianisbova.parceldelivery.domain.model.*;
import org.dzianisbova.parceldelivery.packing.domain.model.ParcelPlacement;
import org.dzianisbova.parceldelivery.packing.domain.model.Position;
import org.dzianisbova.parceldelivery.vehicle.infrastructure.persistence.VehicleEntity;
import org.dzianisbova.parceldelivery.vehicle.infrastructure.persistence.VehicleJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
class DispatchVehicleRepositoryAdapter implements DispatchVehicleRepository {
    private final VehicleJpaRepository vehicleJpaRepository;
    private final PackingPlacementJpaRepository placementJpaRepository;

    @Override
    public List<DispatchVehicle> findAvailable() {
        return vehicleJpaRepository.findAllByStatus(VehicleStatus.AVAILABLE.name())
                .stream()
                .map(entity -> new DispatchVehicle(toVehicle(entity), entity.getPackedVolume()))
                .toList();
    }

    @Override
    public List<ParcelPlacement> findConfirmedPlacements(UUID vehicleId) {
        return placementJpaRepository.findPlacementsByVehicleId(vehicleId)
                .stream()
                .map(this::toParcelPlacement)
                .toList();
    }

    @Override
    public void savePlacements(UUID vehicleId, List<ParcelPlacement> placements, Map<String, UUID> parcelIdToShipmentId) {
        List<PackingPlacementEntity> entities = placements.stream()
                .map(p -> toEntity(vehicleId, p, parcelIdToShipmentId.get(p.parcel().getId())))
                .toList();
        placementJpaRepository.saveAll(entities);
    }

    @Override
    public void deletePlacements(UUID vehicleId) {
        placementJpaRepository.deleteAllByVehicleId(vehicleId);
    }

    private Vehicle toVehicle(VehicleEntity entity) {
        return new Vehicle(
                entity.getId(),
                entity.getPlateNumber(),
                new Dimensions(entity.getLength(), entity.getWidth(), entity.getHeight()),
                entity.getMaxWeight()
        );
    }

    private ParcelPlacement toParcelPlacement(PackingPlacementView view) {
        Parcel parcel = new Parcel(
                view.getParcelId().toString(),
                new Dimensions(view.getLength(), view.getWidth(), view.getHeight()),
                view.getWeight(),
                view.isFragile(),
                Priority.valueOf(view.getPriority())
        );
        return new ParcelPlacement(parcel, new Position(view.getPosX(), view.getPosY(), view.getPosZ()));
    }

    private PackingPlacementEntity toEntity(UUID vehicleId, ParcelPlacement placement, UUID shipmentId) {
        return new PackingPlacementEntity(
                UUID.randomUUID(),
                vehicleId,
                shipmentId,
                placement.position().x(),
                placement.position().y(),
                placement.position().z()
        );
    }
}
