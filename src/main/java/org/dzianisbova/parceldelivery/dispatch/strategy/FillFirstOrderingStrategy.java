package org.dzianisbova.parceldelivery.dispatch.strategy;

import org.dzianisbova.parceldelivery.dispatch.DispatchVehicle;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
class FillFirstOrderingStrategy implements VehicleOrderingStrategy {
    @Override
    public List<DispatchVehicle> sort(List<DispatchVehicle> vehicles) {
        return vehicles.stream()
                .sorted(Comparator.comparingDouble(DispatchVehicle::packedVolume).reversed())
                .toList();
    }
}
