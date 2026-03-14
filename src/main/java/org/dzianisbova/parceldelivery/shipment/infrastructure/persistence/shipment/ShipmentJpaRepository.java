package org.dzianisbova.parceldelivery.shipment.infrastructure.persistence.shipment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShipmentJpaRepository extends JpaRepository<ShipmentEntity, UUID> {
    Optional<ShipmentEntity> findByTrackingNumber(String trackingNumber);

    List<ShipmentEntity> findAllByStatus(String status);
}
