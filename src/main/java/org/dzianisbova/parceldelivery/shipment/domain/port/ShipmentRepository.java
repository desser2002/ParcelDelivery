package org.dzianisbova.parceldelivery.shipment.domain.port;

import org.dzianisbova.parceldelivery.shipment.domain.model.Shipment;
import org.dzianisbova.parceldelivery.shipment.domain.model.ShipmentStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShipmentRepository {
    Shipment save(Shipment shipment);

    Optional<Shipment> findById(UUID id);

    Optional<Shipment> findByTrackingNumber(String trackingNumber);

    List<Shipment> findAllByStatus(ShipmentStatus status);
}
