package org.dzianisbova.parceldelivery.shipment.infrastructure.persistence.tracking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TrackingEventJpaRepository extends JpaRepository<TrackingEventEntity,UUID> {
}
