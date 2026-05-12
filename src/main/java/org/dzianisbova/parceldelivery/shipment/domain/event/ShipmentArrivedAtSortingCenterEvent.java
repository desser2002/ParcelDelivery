package org.dzianisbova.parceldelivery.shipment.domain.event;

import java.time.Instant;
import java.util.UUID;

public record ShipmentArrivedAtSortingCenterEvent(UUID shipmentId,
                                                  UUID sortingCenterId, Instant occurredAt) implements ShipmentEvent {
}
