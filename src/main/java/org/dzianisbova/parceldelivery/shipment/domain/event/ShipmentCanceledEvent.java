package org.dzianisbova.parceldelivery.shipment.domain.event;

import java.time.Instant;
import java.util.UUID;

public record ShipmentCanceledEvent(UUID shipmentId, Instant occurredAt) implements ShipmentEvent {
}
