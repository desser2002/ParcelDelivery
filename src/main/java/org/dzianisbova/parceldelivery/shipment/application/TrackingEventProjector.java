package org.dzianisbova.parceldelivery.shipment.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dzianisbova.parceldelivery.shipment.domain.event.ShipmentAssignedForDeliveryEvent;
import org.dzianisbova.parceldelivery.shipment.domain.event.ShipmentConfirmedEvent;
import org.dzianisbova.parceldelivery.shipment.domain.model.tracking.TrackingEvent;
import org.dzianisbova.parceldelivery.shipment.domain.model.tracking.TrackingEventType;
import org.dzianisbova.parceldelivery.shipment.domain.port.TrackingEventRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
class TrackingEventProjector {
    private final TrackingEventRepository repository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void on(ShipmentConfirmedEvent e) {
        log.info("[TRACKING] CONFIRMED received for shipment {}", e.shipmentId());
        repository.save(TrackingEvent.of(e.shipmentId(), TrackingEventType.CONFIRMED, e.occurredAt(), null));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void on(ShipmentAssignedForDeliveryEvent e) {
        log.info("[TRACKING] ASSIGNED_FOR_DELIVERY received for shipment {} vehicle {}", e.shipmentId(), e.vehicleId());
        repository.save(TrackingEvent.of(e.shipmentId(), TrackingEventType.ASSIGNED_FOR_DELIVERY,
                e.occurredAt(), e.vehicleId()));
    }
}
