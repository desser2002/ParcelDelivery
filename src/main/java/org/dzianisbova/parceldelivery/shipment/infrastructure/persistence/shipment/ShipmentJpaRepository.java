package org.dzianisbova.parceldelivery.shipment.infrastructure.persistence.shipment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ShipmentJpaRepository extends JpaRepository<ShipmentEntity, UUID> {
    Optional<ShipmentEntity> findByTrackingNumber(String trackingNumber);

    @Query(value = "SELECT nextval('tracking_number_seq')", nativeQuery = true)
    Long nextTrackingNumber();
}
