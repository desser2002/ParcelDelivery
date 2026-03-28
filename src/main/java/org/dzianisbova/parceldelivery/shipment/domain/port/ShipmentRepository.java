package org.dzianisbova.parceldelivery.shipment.domain.port;

import org.dzianisbova.parceldelivery.shipment.domain.model.Shipment;

import java.util.Optional;
import java.util.UUID;

public interface ShipmentRepository {
    Shipment save(Shipment shipment);

    Optional<Shipment> findById(UUID id);

    Optional<Shipment> findByTrackingNumber(String trackingNumber);
}
