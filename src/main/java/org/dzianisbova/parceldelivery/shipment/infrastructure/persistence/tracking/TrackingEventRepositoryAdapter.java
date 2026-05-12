package org.dzianisbova.parceldelivery.shipment.infrastructure.persistence.tracking;

import org.dzianisbova.parceldelivery.shipment.domain.model.tracking.TrackingEvent;
import org.dzianisbova.parceldelivery.shipment.domain.port.TrackingEventRepository;
import org.springframework.stereotype.Repository;

@Repository
class TrackingEventRepositoryAdapter implements TrackingEventRepository {
    private final TrackingEventJpaRepository jpaRepository;
    private final TrackingEventMapper mapper;

    public TrackingEventRepositoryAdapter(TrackingEventJpaRepository jpaRepository, TrackingEventMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public TrackingEvent save(TrackingEvent trackingEvent) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(trackingEvent)));
    }
}
