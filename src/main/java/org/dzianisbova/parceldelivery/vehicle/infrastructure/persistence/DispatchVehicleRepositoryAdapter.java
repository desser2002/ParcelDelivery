package org.dzianisbova.parceldelivery.vehicle.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.dzianisbova.parceldelivery.dispatch.DispatchVehicle;
import org.dzianisbova.parceldelivery.dispatch.port.DispatchVehicleRepository;
import org.dzianisbova.parceldelivery.domain.model.VehicleStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
class DispatchVehicleRepositoryAdapter implements DispatchVehicleRepository {
    private final VehicleJpaRepository jpaRepository;
    private final VehicleMapper mapper;

    @Override
    public List<DispatchVehicle> findAvailable() {
        return jpaRepository.findAllByStatus(VehicleStatus.AVAILABLE.name())
                .stream()
                .map(entity -> new DispatchVehicle(mapper.toDomain(entity), entity.getPackedVolume()))
                .toList();
    }
}
