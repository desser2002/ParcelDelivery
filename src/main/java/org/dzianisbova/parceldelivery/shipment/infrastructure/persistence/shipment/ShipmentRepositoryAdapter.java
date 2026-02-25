package org.dzianisbova.parceldelivery.shipment.infrastructure.persistence.shipment;

import lombok.RequiredArgsConstructor;
import org.dzianisbova.parceldelivery.shipment.domain.model.Shipment;
import org.dzianisbova.parceldelivery.shipment.domain.port.ShipmentRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ShipmentRepositoryAdapter implements ShipmentRepository {
    private final ShipmentJpaRepository jpaRepository;
    private final ShipmentMapper mapper;

    @Override
    public Shipment save(Shipment shipment) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(shipment)));
    }

    @Override
    public Optional<Shipment> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Shipment> findByTrackingNumber(String trackingNumber) {
        return jpaRepository.findByTrackingNumber(trackingNumber).map(mapper::toDomain);
    }
}
