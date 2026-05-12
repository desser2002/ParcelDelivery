package org.dzianisbova.parceldelivery.vehicle.infrastructure.persistence;

import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Vehicle;
import org.dzianisbova.parceldelivery.domain.model.VehicleStatus;
import org.springframework.stereotype.Component;

@Component
class VehicleMapper {
    public Vehicle toDomain(VehicleEntity entity) {
        return new Vehicle(
                entity.getId(),
                entity.getPlateNumber(),
                new Dimensions(entity.getLength(), entity.getWidth(), entity.getHeight()),
                entity.getMaxWeight(),
                VehicleStatus.valueOf(entity.getStatus()),
                entity.getPackedVolume()
        );
    }

    public VehicleEntity toEntity(Vehicle vehicle) {
        return new VehicleEntity(vehicle.getId(),
                vehicle.getPlateNumber(),
                vehicle.getDimensions().length(),
                vehicle.getDimensions().width(),
                vehicle.getDimensions().height(),
                vehicle.getMaxWeight(),
                vehicle.getStatus().toString(),
                vehicle.getPackedVolume());
    }
}
