package org.dzianisbova.parceldelivery.shipment.infrastructure.tracking;

import org.dzianisbova.parceldelivery.shipment.domain.port.TrackingNumberGenerator;
import org.dzianisbova.parceldelivery.shipment.infrastructure.persistence.shipment.ShipmentJpaRepository;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HashidsTrackingNumberGenerator implements TrackingNumberGenerator {
    private final ShipmentJpaRepository shipmentJpaRepository;
    private final Hashids hashids;

    public HashidsTrackingNumberGenerator(ShipmentJpaRepository shipmentJpaRepository,
                                          @Value("${tracking.hashids.salt}") String salt,
                                          @Value("${tracking.hashids.min-length}") int minLength) {
        this.shipmentJpaRepository = shipmentJpaRepository;
        this.hashids = new Hashids(salt, minLength, "0123456789ABCDEF");
    }

    @Override
    public String generate() {
        Long sequenceVal = shipmentJpaRepository.nextTrackingNumber();
        return hashids.encode(sequenceVal);
    }
}
