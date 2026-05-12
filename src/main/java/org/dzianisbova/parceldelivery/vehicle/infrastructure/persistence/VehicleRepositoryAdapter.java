package org.dzianisbova.parceldelivery.vehicle.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.dzianisbova.parceldelivery.domain.model.Vehicle;
import org.dzianisbova.parceldelivery.domain.model.VehicleStatus;
import org.dzianisbova.parceldelivery.vehicle.domain.port.VehicleRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
class VehicleRepositoryAdapter implements VehicleRepository {
    private final VehicleJpaRepository jpaRepository;
    private final VehicleMapper mapper;

    @Override
    public Vehicle save(Vehicle vehicle) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(vehicle)));
    }

    @Override
    public void assign(UUID vehicleId) {
        jpaRepository.updateStatus(vehicleId, VehicleStatus.ASSIGNED.name());
    }

    @Override
    public void addPackedVolume(UUID vehicleId, double addedVolume) {
        if (addedVolume <= 0) {
            throw new IllegalArgumentException("Added volume must be positive, got: " + addedVolume);
        }
        jpaRepository.addPackedVolume(vehicleId, addedVolume);
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
