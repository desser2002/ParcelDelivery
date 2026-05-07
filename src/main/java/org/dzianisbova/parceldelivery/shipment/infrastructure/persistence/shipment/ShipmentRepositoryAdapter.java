package org.dzianisbova.parceldelivery.shipment.infrastructure.persistence.shipment;

import lombok.RequiredArgsConstructor;
import org.dzianisbova.parceldelivery.shipment.domain.model.Shipment;
import org.dzianisbova.parceldelivery.shipment.domain.model.ShipmentStatus;
import org.dzianisbova.parceldelivery.shipment.domain.port.ShipmentRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
class ShipmentRepositoryAdapter implements ShipmentRepository {
    private final ShipmentJpaRepository jpaRepository;
    private final ShipmentMapper mapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Shipment save(Shipment shipment) {
        Shipment savedShipment = mapper.toDomain(jpaRepository.save(mapper.toEntity(shipment)));
        shipment.pullEvents().forEach(eventPublisher::publishEvent);
        return savedShipment;
    }

    @Override
    public List<Shipment> saveAll(List<Shipment> shipments) {
        List<Shipment> saved = jpaRepository.saveAll(shipments.stream().map(mapper::toEntity).toList())
                .stream()
                .map(mapper::toDomain)
                .toList();

        shipments.stream()
                .flatMap(shipment -> shipment.pullEvents().stream())
                .forEach(eventPublisher::publishEvent);

        return saved;
    }

    @Override
    public Optional<Shipment> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Shipment> findByTrackingNumber(String trackingNumber) {
        return jpaRepository.findByTrackingNumber(trackingNumber).map(mapper::toDomain);
    }

    @Override
    public List<Shipment> findAllByStatus(ShipmentStatus status) {
        return jpaRepository.findAllByStatus(status.name())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
