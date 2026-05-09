package org.dzianisbova.parceldelivery.vehicle.application;

import lombok.RequiredArgsConstructor;
import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Vehicle;
import org.dzianisbova.parceldelivery.vehicle.domain.port.VehicleRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleService {
    public final VehicleRepository vehicleRepository;

    public Vehicle create(String plateNumber, Dimensions dimensions, double maxWeight) {
        Vehicle vehicle = new Vehicle(plateNumber, dimensions, maxWeight);
        return vehicleRepository.save(vehicle);
    }
}
