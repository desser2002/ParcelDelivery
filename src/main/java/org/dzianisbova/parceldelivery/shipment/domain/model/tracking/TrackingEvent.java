package org.dzianisbova.parceldelivery.shipment.domain.model.tracking;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public record TrackingEvent(UUID id, UUID shipmentId, TrackingEventType type,
                            LocalDateTime occurredAt, UUID actorId,
                            UUID vehicleId, UUID sortingCenterId) {

    public static TrackingEvent generic(UUID shipmentId, TrackingEventType type, Instant occurredAt) {
        return build(shipmentId, type, occurredAt, null, null);
    }

    public static TrackingEvent withVehicle(UUID shipmentId, TrackingEventType type,
                                            Instant occurredAt, UUID vehicleId) {
        return build(shipmentId, type, occurredAt, vehicleId, null);
    }

    public static TrackingEvent withSortingCenter(UUID shipmentId, TrackingEventType type,
                                                  Instant occurredAt, UUID sortingCenterId) {
        return build(shipmentId, type, occurredAt, null, sortingCenterId);
    }

    private static TrackingEvent build(UUID shipmentId, TrackingEventType type,
                                       Instant occurredAt, UUID vehicleId, UUID sortingCenterId) {
        return new TrackingEvent(UUID.randomUUID(), shipmentId, type,
            LocalDateTime.ofInstant(occurredAt, ZoneOffset.UTC),
            null, vehicleId, sortingCenterId);
    }
}
