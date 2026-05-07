package org.dzianisbova.parceldelivery.shipment.domain.model.tracking;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public record TrackingEvent(UUID id, UUID shipmentId, TrackingEventType type,
                            LocalDateTime occurredAt, UUID actorId, UUID vehicleId) {
    public static TrackingEvent of(UUID shipmentId, TrackingEventType type,
                                   Instant occurredAt, UUID vehicleId) {
        return new TrackingEvent(UUID.randomUUID(), shipmentId, type,
                LocalDateTime.ofInstant(occurredAt, ZoneOffset.UTC),
                null, vehicleId);
    }
}
