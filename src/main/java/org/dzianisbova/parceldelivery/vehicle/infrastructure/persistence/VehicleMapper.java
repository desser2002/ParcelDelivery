package org.dzianisbova.parceldelivery.vehicle.infrastructure.persistence;

import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Vehicle;
import org.springframework.stereotype.Component;

@Component
class VehicleMapper {
    public Vehicle toDomain(VehicleEntity entity) {
        return new Vehicle(
                entity.getId(),
                entity.getPlateNumber(),
                new Dimensions(entity.getLength(), entity.getWidth(), entity.getHeight()),
                entity.getMaxWeight()
        );
    }
}
