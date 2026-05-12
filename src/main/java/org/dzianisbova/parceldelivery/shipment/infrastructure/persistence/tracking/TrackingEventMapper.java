package org.dzianisbova.parceldelivery.shipment.infrastructure.persistence.tracking;

import org.dzianisbova.parceldelivery.shipment.domain.model.tracking.TrackingEvent;
import org.springframework.stereotype.Component;

@Component
class TrackingEventMapper {
    public TrackingEvent toDomain(TrackingEventEntity entity) {
        return new TrackingEvent(
            entity.getId(),
            entity.getShipmentId(),
            entity.getType(),
            entity.getOccurredAt(),
            entity.getActorId(),
            entity.getVehicleId(),
            entity.getSortingCenterId()
        );
    }

    public TrackingEventEntity toEntity(TrackingEvent trackingEvent) {
        return new TrackingEventEntity(
            trackingEvent.id(),
            trackingEvent.shipmentId(),
            trackingEvent.type(),
            trackingEvent.occurredAt(),
            trackingEvent.actorId(),
            trackingEvent.vehicleId(),
            trackingEvent.sortingCenterId()
        );
    }
}
