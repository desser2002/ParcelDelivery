package org.dzianisbova.parceldelivery.dispatch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dzianisbova.parceldelivery.dispatch.port.DispatchVehicleRepository;
import org.dzianisbova.parceldelivery.dispatch.strategy.VehicleOrderingStrategy;
import org.dzianisbova.parceldelivery.packing.domain.model.MultiVehiclePackingResult;
import org.dzianisbova.parceldelivery.packing.domain.model.ParcelPlacement;
import org.dzianisbova.parceldelivery.packing.domain.model.VehiclePackingResult;
import org.dzianisbova.parceldelivery.packing.domain.service.MultiVehiclePackingService;
import org.dzianisbova.parceldelivery.shipment.domain.model.Shipment;
import org.dzianisbova.parceldelivery.shipment.domain.model.ShipmentStatus;
import org.dzianisbova.parceldelivery.shipment.domain.port.ShipmentRepository;
import org.dzianisbova.parceldelivery.vehicle.domain.port.VehicleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DispatchService {
    private final MultiVehiclePackingService packingService;
    private final ShipmentRepository shipmentRepository;
    private final DispatchVehicleRepository dispatchVehicleRepository;
    private final VehicleOrderingStrategy vehicleOrderingStrategy;
    private final VehicleRepository vehicleRepository;

    @Value("${packing.vehicle.fill-threshold:0.8}")
    private double fillThreshold;

    @Transactional
    public void dispatch() {
        List<Shipment> pendingShipments = shipmentRepository.findAllByStatus(ShipmentStatus.ARRIVED_AT_SORTING_CENTER);

        if (pendingShipments.isEmpty()) {
            log.debug("[DISPATCH] No pending shipments, skipping");
            return;
        }

        List<DispatchVehicle> availableVehicles = vehicleOrderingStrategy
                .sort(dispatchVehicleRepository.findAvailable());
        if (availableVehicles.isEmpty()) {
            log.warn("[DISPATCH] No available vehicles, pending shipments: {}", pendingShipments.size());
            return;
        }

        log.info("[DISPATCH] Start — pending: {}, vehicles: {}", pendingShipments.size(), availableVehicles.size());
        long dispatchStartTime = System.currentTimeMillis();

        Map<String, Shipment> shipmentByParcelId = pendingShipments.stream()
                .collect(Collectors.toMap(s -> s.getParcel().getId(), s -> s));

        var parcelsToPack = pendingShipments.stream()
                .map(Shipment::getParcel)
                .toList();

        Map<UUID, List<ParcelPlacement>> existingByVehicle = availableVehicles.stream()
                .collect(Collectors.toMap(
                        DispatchVehicle::id,
                        v -> dispatchVehicleRepository.findConfirmedPlacements(v.id())
                ));

        long algorithmStartTime = System.currentTimeMillis();

        MultiVehiclePackingResult result = packingService.packSequentially(
                parcelsToPack,
                availableVehicles.stream()
                        .map(DispatchVehicle::getVehicle)
                        .toList(),
                existingByVehicle);
        log.debug("[DISPATCH] Algorithm finished in {}ms", System.currentTimeMillis() - algorithmStartTime);

        for (VehiclePackingResult vehicleResult : result.vehicleResults()) {
            UUID vehicleId = vehicleResult.vehicleId();
            log.debug("[DISPATCH] Vehicle {} — {} parcels", vehicleId, vehicleResult.placements().size());

            double addedVolume = vehicleResult.placements().stream()
                    .mapToDouble(p -> p.parcel().getDimensions().volume()).sum();

            List<Shipment> confirmed = vehicleResult.placements().stream()
                    .map(p -> {
                        Shipment shipment = shipmentByParcelId.get(p.parcel().getId());
                        shipment.assignForDelivery(vehicleId);
                        return shipment;
                    })
                    .toList();
            shipmentRepository.saveAll(confirmed);

            Map<String, UUID> parcelIdToShipmentId = confirmed.stream()
                    .collect(Collectors.toMap(s -> s.getParcel().getId(), Shipment::getId));
            dispatchVehicleRepository.savePlacements(vehicleId, vehicleResult.placements(), parcelIdToShipmentId);

            vehicleRepository.addPackedVolume(vehicleId, addedVolume);

            DispatchVehicle dispatchVehicle = availableVehicles.stream()
                    .filter(v -> v.id().equals(vehicleId))
                    .findFirst()
                    .orElseThrow();

            DispatchVehicle updatedVehicle = dispatchVehicle.withAddedVolume(addedVolume);

            log.debug("[DISPATCH] Vehicle {} fill: {}%", vehicleId,
                    Math.round(updatedVehicle.fillRatio() * 100));

            if (updatedVehicle.isLoadComplete(fillThreshold)) {
                log.info("[DISPATCH] Vehicle {} reached fill threshold, marking ASSIGNED", vehicleId);
                vehicleRepository.assign(vehicleId);
                dispatchVehicleRepository.deletePlacements(vehicleId);
            } else {
                log.debug("[DISPATCH] Vehicle {} below threshold, keeping AVAILABLE", vehicleId);
            }
        }
        log.info("[DISPATCH] Done — packed: {}, unpacked: {}, vehicles used: {}/{}, total time: {}ms",
                result.getTotalPackedParcels(),
                result.unpackedParcels().size(),
                result.getUsedVehicles(),
                availableVehicles.size(),
                System.currentTimeMillis() - dispatchStartTime);
    }
}
