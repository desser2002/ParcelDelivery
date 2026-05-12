package org.dzianisbova.parceldelivery.shipment.domain.event;

import java.time.Instant;
import java.util.UUID;

public record ShipmentAssignedForDeliveryEvent(UUID shipmentId, UUID vehicleId,
                                               Instant occurredAt) implements ShipmentEvent {}
