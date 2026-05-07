package org.dzianisbova.parceldelivery.shipment.domain.event;

import java.time.Instant;
import java.util.UUID;

public sealed interface ShipmentEvent permits ShipmentConfirmedEvent,ShipmentAssignedForDeliveryEvent {
    UUID shipmentId();

    Instant occurredAt();
}
