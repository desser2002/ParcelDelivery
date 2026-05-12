package org.dzianisbova.parceldelivery.shipment.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dzianisbova.parceldelivery.shipment.domain.event.*;
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
    public void on(ShipmentCreatedEvent e) {
        log.info("[TRACKING] CREATED received for shipment {} ", e.shipmentId());
        repository.save(TrackingEvent.generic(e.shipmentId(), TrackingEventType.CREATED, e.occurredAt()));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void on(ShipmentConfirmedEvent e) {
        log.info("[TRACKING] CONFIRMED received for shipment {}", e.shipmentId());
        repository.save(TrackingEvent.generic(e.shipmentId(), TrackingEventType.CONFIRMED, e.occurredAt()));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void on(ShipmentArrivedAtSortingCenterEvent e) {
        log.info("[TRACKING] ArrivedAtSortingCenter received for shipment {} sortingCenter {}",
            e.shipmentId(), e.sortingCenterId());
        repository.save(TrackingEvent.withSortingCenter(e.shipmentId(),
            TrackingEventType.ARRIVED_AT_SORTING_CENTER, e.occurredAt(), e.sortingCenterId()));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void on(ShipmentAssignedForDeliveryEvent e) {
        log.info("[TRACKING] ASSIGNED_FOR_DELIVERY received for shipment {} vehicle {}",
            e.shipmentId(), e.vehicleId());
        repository.save(TrackingEvent.withVehicle(e.shipmentId(),
            TrackingEventType.ASSIGNED_FOR_DELIVERY, e.occurredAt(), e.vehicleId()));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void on(ShipmentDeliveredEvent e) {
        log.info("[TRACKING] DELIVERED received for shipment {} ", e.shipmentId());
        repository.save(TrackingEvent.generic(e.shipmentId(), TrackingEventType.DELIVERED, e.occurredAt()));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void on(ShipmentCanceledEvent e) {
        log.info("[TRACKING] CANCELED received for shipment {} ", e.shipmentId());
        repository.save(TrackingEvent.generic(e.shipmentId(), TrackingEventType.CANCELLED, e.occurredAt()));
    }
}
