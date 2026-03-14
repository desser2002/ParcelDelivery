package org.dzianisbova.parceldelivery.vehicle.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.dzianisbova.parceldelivery.domain.model.Vehicle;
import org.dzianisbova.parceldelivery.domain.model.VehicleStatus;
import org.dzianisbova.parceldelivery.vehicle.domain.port.VehicleRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
class VehicleRepositoryAdapter implements VehicleRepository {
    private final VehicleJpaRepository jpaRepository;
    private final VehicleMapper mapper;

    @Override
    public List<Vehicle> findAvailable() {
        return jpaRepository.findAllByStatus(VehicleStatus.AVAILABLE.name())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void assign(UUID vehicleId) {
        jpaRepository.updateStatus(vehicleId, VehicleStatus.ASSIGNED.name());
    }
}
