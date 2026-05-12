package org.dzianisbova.parceldelivery.shipment.domain.event;

import java.time.Instant;
import java.util.UUID;

public record ShipmentConfirmedEvent(UUID shipmentId,Instant occurredAt) implements ShipmentEvent {
}
