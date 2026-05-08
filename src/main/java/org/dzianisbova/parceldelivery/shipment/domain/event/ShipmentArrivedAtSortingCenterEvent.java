package org.dzianisbova.parceldelivery.shipment.domain.event;

import java.time.Instant;
import java.util.UUID;

public record ShipmentArrivedAtSortingCenterEvent(UUID shipmentId, Instant occurredAt) implements ShipmentEvent {
}
