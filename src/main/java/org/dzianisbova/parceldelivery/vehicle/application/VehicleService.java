package org.dzianisbova.parceldelivery.vehicle.application;

import lombok.RequiredArgsConstructor;
import org.dzianisbova.parceldelivery.domain.model.Vehicle;
import org.dzianisbova.parceldelivery.vehicle.domain.port.VehicleRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleService {
    public final VehicleRepository vehicleRepository;

    public Vehicle create(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }
}
