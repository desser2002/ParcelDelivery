package org.dzianisbova.parceldelivery.shipment.domain.event;

import java.time.Instant;
import java.util.UUID;

public sealed interface ShipmentEvent permits ShipmentCreatedEvent, ShipmentConfirmedEvent,
        ShipmentAssignedForDeliveryEvent {
    UUID shipmentId();

    Instant occurredAt();
}
