package org.dzianisbova.parceldelivery.assignment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
            log.debug("[PACKING] No pending shipments, skipping");
            return;
        }

        var availableVehicles = vehicleRepository.findAvailable();
        if (availableVehicles.isEmpty()) {
            log.warn("[PACKING] No available vehicles, pending shipments: {}", pendingShipments.size());
            return;
        }

        log.info("[Packing] Start - pending: {}, vehicles: {}", pendingShipments.size(), availableVehicles.size());
        long packingStartTime = System.currentTimeMillis();

        Map<String, Shipment> shipmentByParcelId = pendingShipments.stream()
                .collect(Collectors.toMap(s -> s.getParcel().getId(), s -> s));

        var parcelsToPack = pendingShipments.stream()
                .map(Shipment::getParcel)
                .toList();

        long algorithmStartTime = System.currentTimeMillis();

        MultiVehiclePackingResult result = packingService.packSequentially(parcelsToPack, availableVehicles);
        log.debug("[PACKING] Algorithm finished in {}ms", System.currentTimeMillis() - algorithmStartTime);

        for (VehiclePackingResult vehicleResult : result.vehicleResults()) {
            UUID vehicleId = vehicleResult.vehicleId();
            log.debug("[PACKING] Vehicle {} — {} parcels", vehicleId, vehicleResult.placements().size());

            vehicleResult.placements().forEach(placement -> {
                Shipment shipment = shipmentByParcelId.get(placement.parcel().getId());
                shipment.confirm(vehicleId);
                shipmentRepository.save(shipment);
            });

            vehicleRepository.assign(vehicleId);
        }
        log.info("[PACKING] DONE — packed: {}, unpacked: {}, vehicles used: {}/{}, total time: {}ms",
                result.getTotalPackedParcels(),
                result.unpackedParcels().size(),
                result.getUsedVehicles(),
                availableVehicles.size(),
                System.currentTimeMillis() - packingStartTime);
    }
}
