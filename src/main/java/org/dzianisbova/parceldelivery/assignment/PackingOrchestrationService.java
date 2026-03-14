package org.dzianisbova.parceldelivery.assignment;

import lombok.RequiredArgsConstructor;
import org.dzianisbova.parceldelivery.packing.domain.model.MultiVehiclePackingResult;
import org.dzianisbova.parceldelivery.packing.domain.model.VehiclePackingResult;
import org.dzianisbova.parceldelivery.packing.domain.service.MultiVehiclePackingService;
import org.dzianisbova.parceldelivery.shipment.domain.model.Shipment;
import org.dzianisbova.parceldelivery.shipment.domain.model.ShipmentStatus;
import org.dzianisbova.parceldelivery.shipment.domain.port.ShipmentRepository;
import org.dzianisbova.parceldelivery.vehicle.domain.port.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PackingOrchestrationService {
    private final MultiVehiclePackingService packingService;
    private final ShipmentRepository shipmentRepository;
    private final VehicleRepository vehicleRepository;

    @Transactional
    public void runPacking() {
        List<Shipment> pendingShipments = shipmentRepository.findAllByStatus(ShipmentStatus.PENDING);
        if (pendingShipments.isEmpty()) {
            return;
        }

        var availableVehicles = vehicleRepository.findAvailable();
        if (availableVehicles.isEmpty()) {
            return;
        }

        Map<String, Shipment> shipmentByParcelId = pendingShipments.stream()
                .collect(Collectors.toMap(s -> s.getParcel().getId(), s -> s));

        var parcelsToPack = pendingShipments.stream()
                .map(Shipment::getParcel)
                .toList();

        MultiVehiclePackingResult result = packingService.packSequentially(parcelsToPack, availableVehicles);

        for (VehiclePackingResult vehicleResult : result.vehicleResults()) {
            UUID vehicleId = vehicleResult.vehicleId();

            vehicleResult.placements().forEach(placement -> {
                Shipment shipment = shipmentByParcelId.get(placement.parcel().getId());
                shipment.confirm(vehicleId);
                shipmentRepository.save(shipment);
            });

            vehicleRepository.assign(vehicleId);
        }
    }
}
